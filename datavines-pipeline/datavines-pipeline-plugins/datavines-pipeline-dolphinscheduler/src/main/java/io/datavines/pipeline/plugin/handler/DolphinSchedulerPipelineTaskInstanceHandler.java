package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.QueryColumn;

public class DolphinSchedulerPipelineTaskInstanceHandler extends AbstractPipelineDataResponseHandler {

    @Override
    protected void operateHeader() {
        QueryColumn name = new QueryColumn("name", "varchar(255)");
        QueryColumn taskType = new QueryColumn("task_type", "varchar(50)");
        QueryColumn taskExecuteType = new QueryColumn("task_execute_type", "int(11)");
        QueryColumn taskDefinitionCode = new QueryColumn("task_definition_code", "bigint(20)");
        QueryColumn taskDefinitionVersion = new QueryColumn("task_definition_version", "int(11)");
        QueryColumn dagInstanceId = new QueryColumn("dag_instance_id", "int(11)");
        QueryColumn dagInstanceName = new QueryColumn("dag_instance_name", "varchar(255)");
        QueryColumn projectCode = new QueryColumn("project_code", "bigint(20)");
        QueryColumn state = new QueryColumn("state", "tinyint(4)");
        QueryColumn submitTime = new QueryColumn("submit_time", "datetime");
        QueryColumn startTime = new QueryColumn("start_time", "datetime");
        QueryColumn endTime = new QueryColumn("end_time", "datetime");
        QueryColumn host = new QueryColumn("host", "varchar(135)");
        QueryColumn pid = new QueryColumn("pid", "int(4)");
        QueryColumn appIds = new QueryColumn("app_ids", "text");
        QueryColumn taskParams = new QueryColumn("task_params", "longtext");
        QueryColumn properties = new QueryColumn("properties","longtext");
        QueryColumn creator = new QueryColumn("creator", "varchar(255)");
        QueryColumn createTime = new QueryColumn("create_time", "datetime");
        QueryColumn updateTime = new QueryColumn("update_time", "datetime");

        targetTableHeaderList.add(name);
        targetTableHeaderList.add(taskType);
        targetTableHeaderList.add(taskExecuteType);
        targetTableHeaderList.add(taskDefinitionCode);
        targetTableHeaderList.add(taskDefinitionVersion);
        targetTableHeaderList.add(dagInstanceId);
        targetTableHeaderList.add(dagInstanceName);
        targetTableHeaderList.add(projectCode);
        targetTableHeaderList.add(state);
        targetTableHeaderList.add(submitTime);
        targetTableHeaderList.add(startTime);
        targetTableHeaderList.add(endTime);
        targetTableHeaderList.add(host);
        targetTableHeaderList.add(pid);
        targetTableHeaderList.add(appIds);
        targetTableHeaderList.add(taskParams);
        targetTableHeaderList.add(properties);
        targetTableHeaderList.add(creator);
        targetTableHeaderList.add(createTime);
        targetTableHeaderList.add(updateTime);

        oldColumn2NewColumn.put("process_instance_id","dag_instance_id");
        oldColumn2NewColumn.put("process_instance_name","dag_instance_name");
        oldColumn2NewColumn.put("app_link","app_ids");

        propertiesContainColumns.add("environment_config");
        propertiesContainColumns.add("dry_run");
    }
}
