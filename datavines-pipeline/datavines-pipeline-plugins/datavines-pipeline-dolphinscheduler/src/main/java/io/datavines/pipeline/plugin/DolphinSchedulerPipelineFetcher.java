package io.datavines.pipeline.plugin;

import io.datavines.common.datasource.jdbc.*;
import io.datavines.common.datasource.jdbc.utils.SqlUtils;
import io.datavines.common.entity.ListWithQueryColumn;
import io.datavines.common.utils.JSONUtils;
import io.datavines.pipeline.api.PipelineDataResponse;
import io.datavines.pipeline.api.PipelineFetcher;
import io.datavines.pipeline.api.param.FetchDataParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class DolphinSchedulerPipelineFetcher implements PipelineFetcher {

    private final JdbcExecutorClientManager jdbcExecutorClientManager = JdbcExecutorClientManager.getInstance();

    @Override
    public PipelineDataResponse fetchData(FetchDataParam param) {

        JdbcConnectionInfo jdbcConnectionInfo = JSONUtils.parseObject(param.getPipelineParam(), JdbcConnectionInfo.class);

        BaseJdbcDataSourceInfo dataSourceInfo = getDatasourceInfo(param.getSourceType(), jdbcConnectionInfo);
        if (dataSourceInfo == null) {
            log.error("can not get the datasource info");
            return null;
        }

        JdbcExecutorClient executorClient = jdbcExecutorClientManager
                .getExecutorClient(
                        JdbcDataSourceInfoManager.getDatasourceInfo(param.getPipelineParam(), dataSourceInfo));
        JdbcTemplate jdbcTemplate = executorClient.getJdbcTemplate();

        String sql = "select * from " + param.getTableName() + " where update_time >= " + param.getLastSeenTime() + " order by update_time";

        PipelineDataResponse pipelineDataResponse = new PipelineDataResponse();
        pipelineDataResponse.setTableName(param.getTableName());
        pipelineDataResponse.setSourceUUID(param.getSourceUUID());
        pipelineDataResponse.setResponseDataList(query(jdbcTemplate, sql));

        return pipelineDataResponse;
    }

    private BaseJdbcDataSourceInfo getDatasourceInfo(String type, JdbcConnectionInfo jdbcConnectionInfo) {
        if ("mysql".equalsIgnoreCase(type)) {
            return new MysqlDataSourceInfo(jdbcConnectionInfo);
        } else if ("postgresql".equalsIgnoreCase(type)) {
            return new PostgreSqlDataSourceInfo(jdbcConnectionInfo);
        } else {
            return null;
        }
    }

    private ListWithQueryColumn query(JdbcTemplate jdbcTemplate, String sql) {
        return SqlUtils.query(jdbcTemplate, sql);
    }
}
