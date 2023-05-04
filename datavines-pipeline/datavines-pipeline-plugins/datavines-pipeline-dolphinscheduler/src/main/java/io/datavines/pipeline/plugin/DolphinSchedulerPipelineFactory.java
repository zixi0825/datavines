package io.datavines.pipeline.plugin;

import io.datavines.pipeline.api.PipelineConnector;
import io.datavines.pipeline.api.PipelineFactory;
import io.datavines.pipeline.api.PipelineFetcher;

public class DolphinSchedulerPipelineFactory implements PipelineFactory {

    @Override
    public PipelineFetcher getPipelineFetcher() {
        return new DolphinSchedulerPipelineFetcher();
    }

    @Override
    public PipelineConnector getPipelineConnector() {
        return new DolphinSchedulerPipelineConnector();
    }
}
