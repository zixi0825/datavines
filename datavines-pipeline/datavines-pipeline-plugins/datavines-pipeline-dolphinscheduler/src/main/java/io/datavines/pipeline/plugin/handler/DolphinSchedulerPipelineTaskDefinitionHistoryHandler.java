package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.QueryColumn;

public class DolphinSchedulerPipelineTaskDefinitionHistoryHandler extends AbstractPipelineDataResponseHandler {

    @Override
    protected void operateHeader() {
        QueryColumn name = new QueryColumn("name", "varchar(255)");
        QueryColumn code = new QueryColumn("code", "bigint(20)");
        QueryColumn version = new QueryColumn("version", "int(11)");
        QueryColumn description = new QueryColumn("description", "varchar(255)");
        QueryColumn projectCode = new QueryColumn("project_code", "bigint(20)");
        QueryColumn taskType = new QueryColumn("task_type", "varchar(50)");
        QueryColumn taskExecuteType = new QueryColumn("task_execute_type", "int(11)");
        QueryColumn taskParams = new QueryColumn("task_params", "longtext");
        QueryColumn properties = new QueryColumn("properties","longtext");
        QueryColumn operator = new QueryColumn("operator", "varchar(255)");
        QueryColumn operateTime = new QueryColumn("operate_time", "datetime");
        QueryColumn creator = new QueryColumn("creator", "varchar(255)");
        QueryColumn createTime = new QueryColumn("create_time", "datetime");
        QueryColumn updateTime = new QueryColumn("update_time", "datetime");

        targetTableHeaderList.add(name);
        targetTableHeaderList.add(code);
        targetTableHeaderList.add(version);
        targetTableHeaderList.add(description);
        targetTableHeaderList.add(projectCode);
        targetTableHeaderList.add(taskType);
        targetTableHeaderList.add(taskExecuteType);
        targetTableHeaderList.add(taskParams);
        targetTableHeaderList.add(properties);
        targetTableHeaderList.add(operator);
        targetTableHeaderList.add(operateTime);
        targetTableHeaderList.add(creator);
        targetTableHeaderList.add(createTime);
        targetTableHeaderList.add(updateTime);

        propertiesContainColumns.add("fail_retry_times");
        propertiesContainColumns.add("fail_retry_interval");
        propertiesContainColumns.add("timeout_flag");
        propertiesContainColumns.add("timeout_notify_strategy");
        propertiesContainColumns.add("timeout");
        propertiesContainColumns.add("delay_time");
    }
}
