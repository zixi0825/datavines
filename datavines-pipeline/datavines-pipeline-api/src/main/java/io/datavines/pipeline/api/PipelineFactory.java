package io.datavines.pipeline.api;

import io.datavines.spi.SPI;

@SPI
public interface PipelineFactory {

    PipelineFetcher getPipelineFetcher();

    PipelineConnector getPipelineConnector();
}
