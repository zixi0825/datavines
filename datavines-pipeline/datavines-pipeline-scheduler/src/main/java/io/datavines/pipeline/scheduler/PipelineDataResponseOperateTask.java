package io.datavines.pipeline.scheduler;

import io.datavines.pipeline.api.PipelineDataResponse;

public class PipelineDataResponseOperateTask implements Runnable {

    private final PipelineDataResponse response;

    public PipelineDataResponseOperateTask(PipelineDataResponse response) {
        this.response = response;
    }

    @Override
    public void run() {
        // response 格式判断
        // response 格式正确，进行数据处理
        // response 格式错误，记录错误日志
        // response handler
    }
}
