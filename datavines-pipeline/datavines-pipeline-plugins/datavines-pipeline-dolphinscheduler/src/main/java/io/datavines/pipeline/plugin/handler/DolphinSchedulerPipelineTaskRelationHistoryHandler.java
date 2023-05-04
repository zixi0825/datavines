package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.QueryColumn;

public class DolphinSchedulerPipelineTaskRelationHistoryHandler extends AbstractPipelineDataResponseHandler {

    @Override
    protected void operateHeader() {
        QueryColumn projectCode = new QueryColumn("project_code", "bigint(20)");
        QueryColumn dagDefinitionCode = new QueryColumn("dag_definition_code", "bigint(20)");
        QueryColumn dagDefinitionVersion = new QueryColumn("dag_definition_version", "int(11)");
        QueryColumn preTaskCode = new QueryColumn("pre_task_code", "bigint(20)");
        QueryColumn preTaskVersion = new QueryColumn("pre_task_version", "int(11)");
        QueryColumn postTaskCode = new QueryColumn("post_task_code", "bigint(20)");
        QueryColumn postTaskVersion = new QueryColumn("post_task_version", "int(11)");
        QueryColumn conditionType = new QueryColumn("condition_type", "tinyint(2)");
        QueryColumn conditionParams = new QueryColumn("condition_params", "text");
        QueryColumn properties = new QueryColumn("properties","longtext");
        QueryColumn operator = new QueryColumn("operator", "varchar(255)");
        QueryColumn operateTime = new QueryColumn("operate_time", "datetime");
        QueryColumn creator = new QueryColumn("creator", "varchar(255)");
        QueryColumn createTime = new QueryColumn("create_time", "datetime");
        QueryColumn updateTime = new QueryColumn("update_time", "datetime");

        targetTableHeaderList.add(projectCode);
        targetTableHeaderList.add(dagDefinitionCode);
        targetTableHeaderList.add(dagDefinitionVersion);
        targetTableHeaderList.add(preTaskCode);
        targetTableHeaderList.add(preTaskVersion);
        targetTableHeaderList.add(postTaskCode);
        targetTableHeaderList.add(postTaskVersion);
        targetTableHeaderList.add(conditionType);
        targetTableHeaderList.add(conditionParams);
        targetTableHeaderList.add(properties);
        targetTableHeaderList.add(operator);
        targetTableHeaderList.add(operateTime);
        targetTableHeaderList.add(creator);
        targetTableHeaderList.add(createTime);
        targetTableHeaderList.add(updateTime);

        oldColumn2NewColumn.put("process_definition_code","dag_definition_code");
        oldColumn2NewColumn.put("process_definition_version","dag_definition_version");
    }
}
