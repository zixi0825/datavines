package io.datavines.pipeline.plugin.dolphinscheduler.handler;

import io.datavines.common.entity.ListWithQueryColumn;
import io.datavines.common.entity.QueryColumn;
import io.datavines.pipeline.api.PipelineDataResponse;
import org.apache.commons.collections4.CollectionUtils;

public class PipelineProjectTableHandler extends AbstractPipelineDataResponseHandler {

    @Override
    public void handle(PipelineDataResponse response) {
        super.handle(response);
        if (response != null
                && response.getResponseDataList() != null
                && CollectionUtils.isNotEmpty(response.getResponseDataList().getResultList())) {
            ListWithQueryColumn list = response.getResponseDataList();
            list.setColumns(targetTableHeaderList);
        }
        // 取出ResponseData, 替换Header，然后根据
    }

    @Override
    protected void operateHeader() {
        QueryColumn name = new QueryColumn("name","varchar(255)");
        QueryColumn code = new QueryColumn("code","bigint(20)");
        QueryColumn description = new QueryColumn("description","varchar(255)");
        QueryColumn creator = new QueryColumn("creator","bigint(20)");
        QueryColumn createTime = new QueryColumn("create_time","varchar(255)");
        QueryColumn updateTime = new QueryColumn("update_time","bigint(20)");

        targetTableHeaderList.add(name);
        targetTableHeaderList.add(code);
        targetTableHeaderList.add(description);
        targetTableHeaderList.add(creator);
        targetTableHeaderList.add(createTime);
        targetTableHeaderList.add(updateTime);

        oldColumn2NewColumn.put("name", "name");
        oldColumn2NewColumn.put("code", "code");
        oldColumn2NewColumn.put("description", "description");
        oldColumn2NewColumn.put("creator", "creator");
        oldColumn2NewColumn.put("create_time", "create_time");
        oldColumn2NewColumn.put("update_time", "update_time");
    }
}
