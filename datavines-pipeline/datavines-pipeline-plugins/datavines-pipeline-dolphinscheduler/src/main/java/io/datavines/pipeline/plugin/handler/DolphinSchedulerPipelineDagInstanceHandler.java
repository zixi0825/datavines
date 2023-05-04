package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.QueryColumn;

public class DolphinSchedulerPipelineDagInstanceHandler extends AbstractPipelineDataResponseHandler {

    @Override
    protected void operateHeader() {
        QueryColumn name = new QueryColumn("name", "varchar(255)");
        QueryColumn projectCode = new QueryColumn("project_code", "bigint(20)");
        QueryColumn dagDefinitionCode = new QueryColumn("dag_definition_code", "bigint(20)");
        QueryColumn dagDefinitionVersion = new QueryColumn("dag_definition_version", "int(11)");
        QueryColumn state = new QueryColumn("state", "tinyint(4)");
        QueryColumn host = new QueryColumn("host", "varchar(135)");
        QueryColumn properties = new QueryColumn("properties","text");
        QueryColumn isSubDag = new QueryColumn("is_sub_dag", "varchar(255)");
        QueryColumn startTime = new QueryColumn("start_time", "datetime");
        QueryColumn endTime = new QueryColumn("end_time", "datetime");
        QueryColumn scheduleTime = new QueryColumn("schedule_time", "datetime");
        QueryColumn updateTime = new QueryColumn("update_time", "datetime");

        targetTableHeaderList.add(name);
        targetTableHeaderList.add(projectCode);
        targetTableHeaderList.add(dagDefinitionCode);
        targetTableHeaderList.add(dagDefinitionVersion);
        targetTableHeaderList.add(state);
        targetTableHeaderList.add(host);
        targetTableHeaderList.add(properties);
        targetTableHeaderList.add(isSubDag);
        targetTableHeaderList.add(startTime);
        targetTableHeaderList.add(endTime);
        targetTableHeaderList.add(scheduleTime);
        targetTableHeaderList.add(updateTime);

        oldColumn2NewColumn.put("process_definition_code","dag_definition_code");
        oldColumn2NewColumn.put("process_definition_version","dag_definition_version");
        oldColumn2NewColumn.put("is_sub_process","is_sub_dag");

        propertiesContainColumns.add("run_times");
        propertiesContainColumns.add("task_depend_type");
        propertiesContainColumns.add("max_try_times");
        propertiesContainColumns.add("failure_strategy");
        propertiesContainColumns.add("global_params");
        propertiesContainColumns.add("timeout");
        propertiesContainColumns.add("tenant_code");
        propertiesContainColumns.add("var_pool");
        propertiesContainColumns.add("dry_run");
        propertiesContainColumns.add("next_process_instance_id");
    }
}
