package io.datavines.pipeline.scheduler;

import io.datavines.pipeline.api.PipelineDataResponse;

import java.util.concurrent.LinkedBlockingDeque;

public class PipelineDataResponseQueue {

    private static class Singleton {
        static PipelineDataResponseQueue instance = new PipelineDataResponseQueue();
    }

    public static PipelineDataResponseQueue getInstance() {
        return PipelineDataResponseQueue.Singleton.instance;
    }

    private final LinkedBlockingDeque<PipelineDataResponse> dataResponseQueue = new LinkedBlockingDeque<>(10000);

    public void put(PipelineDataResponse response) throws InterruptedException {
        dataResponseQueue.put(response);
    }

    public PipelineDataResponse take() throws InterruptedException {
        return dataResponseQueue.take();
    }
}
