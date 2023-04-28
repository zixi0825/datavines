package io.datavines.pipeline.api.param;

import lombok.Data;

@Data
public class FetchDataParam {

    String sourceUUID;

    String sourceType;

    String pipelineParam;

    String tableName;

    String lastSeenTime;
}
