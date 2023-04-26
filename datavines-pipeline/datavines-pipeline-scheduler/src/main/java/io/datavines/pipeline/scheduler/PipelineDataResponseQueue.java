package io.datavines.pipeline.scheduler;

import java.util.concurrent.LinkedBlockingDeque;

public class PipelineDataResponseQueue {

    private final LinkedBlockingDeque<PipelineDataFetchResponse> dataResponseQueue = new LinkedBlockingDeque<>();

    public void put(PipelineDataFetchResponse response) {
        dataResponseQueue.add(response);
    }
}
