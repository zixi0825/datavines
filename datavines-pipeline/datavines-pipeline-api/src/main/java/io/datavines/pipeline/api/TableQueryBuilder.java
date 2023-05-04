package io.datavines.pipeline.api;

import io.datavines.spi.SPI;

@SPI
public interface TableQueryBuilder {
    String buildQuery(String lastSeenTime);

    String tableName();
}
