package io.datavines.pipeline.plugin.dolphinscheduler.handler;

import io.datavines.common.entity.QueryColumn;
import io.datavines.pipeline.api.PipelineDataResponse;
import io.datavines.pipeline.api.PipelineDataResponseHandler;

import java.util.List;
import java.util.Map;

public abstract class AbstractPipelineDataResponseHandler implements PipelineDataResponseHandler {

    protected List<QueryColumn> targetTableHeaderList;

    protected Map<String,String> oldColumn2NewColumn;

    protected abstract void operateHeader();

    @Override
    public void handle(PipelineDataResponse response) {
        operateHeader();
    }
}
