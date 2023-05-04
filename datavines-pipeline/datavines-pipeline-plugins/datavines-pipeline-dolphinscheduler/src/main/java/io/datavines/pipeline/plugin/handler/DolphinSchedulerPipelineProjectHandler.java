package io.datavines.pipeline.plugin.handler;

import io.datavines.common.entity.QueryColumn;

public class DolphinSchedulerPipelineProjectHandler extends AbstractPipelineDataResponseHandler {

    @Override
    protected void operateHeader() {
        QueryColumn name = new QueryColumn("name","varchar(255)");
        QueryColumn code = new QueryColumn("code","bigint(20)");
        QueryColumn description = new QueryColumn("description","varchar(255)");
        QueryColumn properties = new QueryColumn("properties","text");
        QueryColumn creator = new QueryColumn("creator","varchar(255)");
        QueryColumn createTime = new QueryColumn("create_time","datetime");
        QueryColumn updateTime = new QueryColumn("update_time","datetime");

        targetTableHeaderList.add(name);
        targetTableHeaderList.add(code);
        targetTableHeaderList.add(description);
        targetTableHeaderList.add(properties);
        targetTableHeaderList.add(creator);
        targetTableHeaderList.add(createTime);
        targetTableHeaderList.add(updateTime);

        propertiesContainColumns.add("flag");
    }
}
