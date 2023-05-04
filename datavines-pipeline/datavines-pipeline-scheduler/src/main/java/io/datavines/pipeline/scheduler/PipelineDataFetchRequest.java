package io.datavines.pipeline.scheduler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PipelineDataFetchRequest {

    private String sourceUUID;

    private String sourceType;

    private String connectionType;

    private String pipelineSourceParam;

    private String tableName;

    private String lastSeenTime;
}
