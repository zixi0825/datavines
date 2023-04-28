package io.datavines.pipeline.api;

import io.datavines.pipeline.api.param.FetchDataParam;

public interface PipelineFetcher {

    PipelineDataResponse fetchData(FetchDataParam param);
}
