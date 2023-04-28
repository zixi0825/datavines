package io.datavines.pipeline.api;

import io.datavines.common.entity.ListWithQueryColumn;
import lombok.Data;

@Data
public class PipelineDataResponse {

    private String sourceUUID;

    private String tableName;

    private ListWithQueryColumn responseDataList;
}
