package io.datavines.pipeline.api.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchDataParam {

    String sourceUUID;

    String sourceType;

    String connectionType;

    String pipelineParam;

    String tableName;

    String lastSeenTime;
}
