package io.datavines.pipeline.scheduler;

import io.datavines.common.entity.ListWithQueryColumn;
import lombok.Data;

@Data
public class PipelineDataFetchResponse {

    private Long sourceId;

    private String tableName;

    private ListWithQueryColumn responseDataList;
}
