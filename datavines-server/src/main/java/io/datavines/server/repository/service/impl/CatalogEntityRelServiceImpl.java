/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.datavines.server.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.datavines.common.datasource.jdbc.JdbcConnectionInfo;
import io.datavines.common.enums.EntityRelType;
import io.datavines.common.utils.JSONUtils;
import io.datavines.common.utils.StringUtils;
import io.datavines.connector.api.ConnectorFactory;
import io.datavines.connector.api.LineageParser;
import io.datavines.connector.api.entity.ScriptMetadata;
import io.datavines.connector.api.entity.StatementMetadata;
import io.datavines.connector.api.entity.StatementMetadataFragment;
import io.datavines.core.enums.Status;
import io.datavines.core.exception.DataVinesServerException;
import io.datavines.server.api.dto.bo.catalog.CatalogEntityInstanceInfo;
import io.datavines.server.api.dto.bo.catalog.lineage.EntityEdgeInfo;
import io.datavines.server.api.dto.bo.catalog.lineage.SqlWithDataSourceKeyProperties;
import io.datavines.server.api.dto.bo.catalog.lineage.SqlWithDataSourceList;
import io.datavines.server.api.dto.bo.datasource.DataSourceInfo;
import io.datavines.server.api.dto.vo.catalog.lineage.CatalogEntityLineageVO;
import io.datavines.server.enums.LineageSourceType;
import io.datavines.server.repository.entity.DataSource;
import io.datavines.server.repository.entity.catalog.CatalogEntityInstance;
import io.datavines.server.repository.entity.catalog.CatalogEntityRel;
import io.datavines.server.repository.mapper.CatalogEntityRelMapper;
import io.datavines.server.repository.service.CatalogEntityInstanceService;
import io.datavines.server.repository.service.CatalogEntityRelService;
import io.datavines.server.repository.service.DataSourceService;
import io.datavines.server.utils.ContextHolder;
import io.datavines.spi.PluginLoader;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("catalogEntityRelService")
public class CatalogEntityRelServiceImpl extends ServiceImpl<CatalogEntityRelMapper, CatalogEntityRel> implements CatalogEntityRelService {

    @Autowired
    private CatalogEntityInstanceService catalogEntityInstanceService;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public boolean addLineage(EntityEdgeInfo entityEdgeInfo) {
        String fromUUID = entityEdgeInfo.getFromEntity().getUuid();
        String toUUID = entityEdgeInfo.getToEntity().getUuid();

        return addLineage(fromUUID, toUUID, entityEdgeInfo.getLineageDetail().getSourceType(),entityEdgeInfo.getLineageDetail().getSqlQuery());
    }

    @Override
    public boolean addLineageByParseSql(SqlWithDataSourceList sqlWithDataSourceList) {
        if (sqlWithDataSourceList == null) {
            return false;
        }

        List<DataSourceInfo> dataSourceInfos = sqlWithDataSourceList.getDataSourceInfos();
        if (CollectionUtils.isEmpty(dataSourceInfos)) {
            return false;
        }

        String sql = sqlWithDataSourceList.getSql();

        for (DataSourceInfo dataSourceInfo: dataSourceInfos) {
            ConnectorFactory connectorFactory = PluginLoader.getPluginLoader(ConnectorFactory.class).getOrCreatePlugin(dataSourceInfo.getType());
            if (connectorFactory == null) {
                continue;
            }

            DataSource dataSource = dataSourceService.getDataSourceById(dataSourceInfo.getId());

            JdbcConnectionInfo jdbcConnectionInfo = JSONUtils.parseObject(dataSource.getParam(), JdbcConnectionInfo.class);

            ScriptMetadata scriptMetadata = LineageParser.parseScript(sql, connectorFactory.getStatementSplitter(), connectorFactory.getStatementParser());
            if (scriptMetadata == null) {
                continue;
            }

            List<StatementMetadata> statementMetadataList = scriptMetadata.getStatementMetadataList();
            if (CollectionUtils.isEmpty(statementMetadataList)) {
                continue;
            }

            for (StatementMetadata statementMetadata: statementMetadataList) {
                StatementMetadataFragment statementMetadataFragment = statementMetadata.getStatementMetadataFragment();
                if (statementMetadataFragment == null) {
                    continue;
                }

                List<String> inputTables = statementMetadataFragment.getInputTables();
                List<String> outputTables = statementMetadataFragment.getOutputTables();
                if (CollectionUtils.isEmpty(inputTables) || CollectionUtils.isEmpty(outputTables)) {
                    continue;
                }

                for (String inputTable : inputTables) {
                    for (String outputTable : outputTables) {
                        if (StringUtils.isEmpty(inputTable) || StringUtils.isEmpty(outputTable)) {
                            continue;
                        }

                        if (jdbcConnectionInfo!= null && !inputTable.contains(".")) {
                            inputTable = jdbcConnectionInfo.getDatabase() + "." + inputTable;
                        }

                        if (jdbcConnectionInfo!= null && !outputTable.contains(".")) {
                            outputTable = jdbcConnectionInfo.getDatabase() + "." + outputTable;
                        }

                        CatalogEntityInstance fromEntity = catalogEntityInstanceService.getByDataSourceAndFQN(dataSourceInfo.getId(), inputTable);
                        CatalogEntityInstance toEntity = catalogEntityInstanceService.getByDataSourceAndFQN(dataSourceInfo.getId(), outputTable);
                        if (fromEntity == null || toEntity == null) {
                            continue;
                        }

                        return addLineage(fromEntity.getUuid(), toEntity.getUuid(), LineageSourceType.SQL_PARSER, statementMetadata.getStatementText());
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean addLineageByParseSql2(SqlWithDataSourceKeyProperties sqlWithDataSourceKeyProperties) {
        List<DataSourceInfo> dataSourceInfos = dataSourceService.listByInfo(sqlWithDataSourceKeyProperties.getDataSourceKeyProperties());
        SqlWithDataSourceList sqlWithDataSourceList = new SqlWithDataSourceList();
        sqlWithDataSourceList.setSql(sqlWithDataSourceKeyProperties.getSql());
        sqlWithDataSourceList.setDataSourceInfos(dataSourceInfos);
        return addLineageByParseSql(sqlWithDataSourceList);
    }

    @Override
    public CatalogEntityLineageVO getLineageByFqn(Long datasourceId, String fqn, int upstreamDepth, int downstreamDepth) {
        CatalogEntityInstance catalogEntityInstance = catalogEntityInstanceService.getByDataSourceAndFQN(datasourceId, fqn);
        if (catalogEntityInstance == null) {
            throw new DataVinesServerException(Status.CATALOG_PROFILE_INSTANCE_IS_NULL_ERROR,fqn);
        }

        return getCatalogEntityLineageVO(catalogEntityInstance);
    }

    @NotNull
    private CatalogEntityLineageVO getCatalogEntityLineageVO(CatalogEntityInstance catalogEntityInstance) {
        CatalogEntityLineageVO catalogEntityLineageVO = new CatalogEntityLineageVO();
        CatalogEntityInstanceInfo currentEntityInstanceInfo = new CatalogEntityInstanceInfo();
        BeanUtils.copyProperties(catalogEntityInstance, currentEntityInstanceInfo);
        catalogEntityLineageVO.setCurrentEntity(currentEntityInstanceInfo);
        String fromUUID = catalogEntityInstance.getUuid();
        List<CatalogEntityRel> downstreamRelList = list(new LambdaQueryWrapper<CatalogEntityRel>()
                .eq(CatalogEntityRel::getEntity1Uuid, fromUUID)
                .eq(CatalogEntityRel::getType, EntityRelType.DOWNSTREAM.getDescription()));
        if (CollectionUtils.isNotEmpty(downstreamRelList)) {
            List<EntityEdgeInfo> downstreamEdges = new ArrayList<>();
            for (CatalogEntityRel rel : downstreamRelList) {
                CatalogEntityInstance downstreamEntityInstance = catalogEntityInstanceService.getByUUID(rel.getEntity2Uuid());
                if (downstreamEntityInstance != null) {
                    EntityEdgeInfo entityEdgeInfo = new EntityEdgeInfo();
                    CatalogEntityInstanceInfo fromEntity = new CatalogEntityInstanceInfo();
                    BeanUtils.copyProperties(currentEntityInstanceInfo, fromEntity);
                    CatalogEntityInstanceInfo toEntity = new CatalogEntityInstanceInfo();
                    BeanUtils.copyProperties(downstreamEntityInstance, toEntity);
                    entityEdgeInfo.setFromEntity(fromEntity);
                    entityEdgeInfo.setToEntity(toEntity);
                    downstreamEdges.add(entityEdgeInfo);
                }
            }
            catalogEntityLineageVO.setDownstreamEdges(downstreamEdges);
        }

        List<CatalogEntityRel> upstreamRelList = list(new LambdaQueryWrapper<CatalogEntityRel>().eq(CatalogEntityRel::getEntity2Uuid, fromUUID)
                .eq(CatalogEntityRel::getType, EntityRelType.DOWNSTREAM.getDescription()));
        if (CollectionUtils.isNotEmpty(upstreamRelList)) {
            List<EntityEdgeInfo> upstreamEdges = new ArrayList<>();
            for (CatalogEntityRel rel : upstreamRelList) {
                CatalogEntityInstance upstreamEntityInstance = catalogEntityInstanceService.getByUUID(rel.getEntity1Uuid());
                if (upstreamEntityInstance != null) {
                    EntityEdgeInfo entityEdgeInfo = new EntityEdgeInfo();
                    CatalogEntityInstanceInfo fromEntity = new CatalogEntityInstanceInfo();
                    BeanUtils.copyProperties(upstreamEntityInstance, fromEntity);
                    CatalogEntityInstanceInfo toEntity = new CatalogEntityInstanceInfo();
                    BeanUtils.copyProperties(currentEntityInstanceInfo, toEntity);
                    entityEdgeInfo.setFromEntity(fromEntity);
                    entityEdgeInfo.setToEntity(toEntity);
                    upstreamEdges.add(entityEdgeInfo);
                }
            }
            catalogEntityLineageVO.setUpstreamEdges(upstreamEdges);
        }
        return catalogEntityLineageVO;
    }

    @Override
    public CatalogEntityLineageVO getLineageByUUID(String uuid, int upstreamDepth, int downstreamDepth) {
        CatalogEntityInstance catalogEntityInstance = catalogEntityInstanceService.getByUUID(uuid);
        if (catalogEntityInstance == null) {
            throw new DataVinesServerException(Status.CATALOG_PROFILE_INSTANCE_IS_NULL_ERROR, uuid);
        }

        return getCatalogEntityLineageVO(catalogEntityInstance);
    }

    @Override
    public boolean deleteLineage(String fromUUID, String toUUID) {
        return remove(new LambdaQueryWrapper<CatalogEntityRel>().eq(CatalogEntityRel::getEntity1Uuid, fromUUID).eq(CatalogEntityRel::getEntity2Uuid, toUUID));
    }

    private boolean addLineage(String fromUUID, String toUUID, LineageSourceType sourceType, String sql) {
        List<CatalogEntityRel> relList = list(new QueryWrapper<CatalogEntityRel>()
                .eq("entity1_uuid", fromUUID)
                .eq("entity2_uuid", toUUID).eq("type", EntityRelType.DOWNSTREAM.getDescription()));
        CatalogEntityRel rel;
        if (CollectionUtils.isEmpty(relList)) {
            rel = new CatalogEntityRel();
        } else {
            rel = relList.get(0);
        }
        rel.setEntity1Uuid(fromUUID);
        rel.setEntity2Uuid(toUUID);
        rel.setType(EntityRelType.DOWNSTREAM.getDescription());
        rel.setSourceType(sourceType.getDescription());
        rel.setRelatedScript(sql);
        rel.setUpdateBy(ContextHolder.getUserId());
        rel.setUpdateTime(LocalDateTime.now());
        if (CollectionUtils.isEmpty(relList)) {
            return save(rel);
        } else {
            return updateById(rel);
        }
    }

}
