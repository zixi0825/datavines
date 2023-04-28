package io.datavines.pipeline.scheduler;

import io.datavines.pipeline.api.PipelineDataResponse;

import java.util.concurrent.LinkedBlockingDeque;

public class PipelineDataResponseQueue {

    private final LinkedBlockingDeque<PipelineDataResponse> dataResponseQueue = new LinkedBlockingDeque<>();

    public void put(PipelineDataResponse response) throws InterruptedException {
        dataResponseQueue.put(response);
    }

    public PipelineDataResponse take() throws InterruptedException {
        return dataResponseQueue.take();
    }
}
