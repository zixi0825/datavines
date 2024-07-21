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
import io.datavines.common.enums.EntityRelType;
import io.datavines.core.enums.Status;
import io.datavines.core.exception.DataVinesServerException;
import io.datavines.server.api.dto.bo.catalog.CatalogEntityInstanceInfo;
import io.datavines.server.api.dto.bo.catalog.lineage.EntityEdgeInfo;
import io.datavines.server.api.dto.vo.catalog.lineage.CatalogEntityLineageVO;
import io.datavines.server.repository.entity.catalog.CatalogEntityInstance;
import io.datavines.server.repository.entity.catalog.CatalogEntityRel;
import io.datavines.server.repository.mapper.CatalogEntityRelMapper;
import io.datavines.server.repository.service.CatalogEntityInstanceService;
import io.datavines.server.repository.service.CatalogEntityRelService;
import io.datavines.server.utils.ContextHolder;
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

    @Override
    public boolean addLineage(EntityEdgeInfo entityEdgeInfo) {
        String fromUUID = entityEdgeInfo.getFromEntity().getUuid();
        String toUUID = entityEdgeInfo.getToEntity().getUuid();
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
        rel.setSourceType(entityEdgeInfo.getLineageDetail().getSourceType().getDescription());
        rel.setRelatedScript(entityEdgeInfo.getLineageDetail().getSqlQuery());
        rel.setUpdateBy(ContextHolder.getUserId());
        rel.setUpdateTime(LocalDateTime.now());
        if (CollectionUtils.isEmpty(relList)) {
            return save(rel);
        } else {
            return updateById(rel);
        }
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

}
