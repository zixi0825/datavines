package io.datavines.pipeline.scheduler;

import java.util.concurrent.LinkedBlockingDeque;

public class PipelineDataFetchRequestQueue {

    private static class Singleton {
        static PipelineDataFetchRequestQueue instance = new PipelineDataFetchRequestQueue();
    }

    public static PipelineDataFetchRequestQueue getInstance() {
        return PipelineDataFetchRequestQueue.Singleton.instance;
    }

    private final LinkedBlockingDeque<PipelineDataFetchRequest> dataResponseQueue = new LinkedBlockingDeque<>(10000);

    public void put(PipelineDataFetchRequest request) throws InterruptedException {
        dataResponseQueue.put(request);
    }

    public PipelineDataFetchRequest take() throws InterruptedException {
        return dataResponseQueue.take();
    }
}
