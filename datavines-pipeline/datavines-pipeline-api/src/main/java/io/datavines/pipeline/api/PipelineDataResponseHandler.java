package io.datavines.pipeline.api;

import io.datavines.spi.SPI;

@SPI
public interface PipelineDataResponseHandler {

    void handle(PipelineDataResponse response);
}
