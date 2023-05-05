package io.datavines.pipeline.scheduler;

import io.datavines.pipeline.api.PipelineDataResponse;
import io.datavines.pipeline.api.PipelineFactory;
import io.datavines.pipeline.api.param.FetchDataParam;
import io.datavines.spi.PluginLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PipelineDataFetchTaskRunner implements Runnable {

    private final PipelineDataFetchRequest pipelineDataFetchRequest;

    public PipelineDataFetchTaskRunner(PipelineDataFetchRequest pipelineDataFetchRequest) {
        this.pipelineDataFetchRequest = pipelineDataFetchRequest;
    }

    @Override
    public void run() {
        if (pipelineDataFetchRequest == null) {
            return;
        }

        String sourceType = pipelineDataFetchRequest.getSourceType();
        String connectionType = pipelineDataFetchRequest.getConnectionType();
        String pipelineSourceParam = pipelineDataFetchRequest.getPipelineSourceParam();
        String tableName = pipelineDataFetchRequest.getTableName();
        String lastSeenTime = pipelineDataFetchRequest.getLastSeenTime();

        PipelineFactory pipelineFactory = PluginLoader.getPluginLoader(PipelineFactory.class).getOrCreatePlugin(sourceType);
        if (pipelineFactory == null) {
            return;
        }

        FetchDataParam param = FetchDataParam.builder()
                .sourceUUID(pipelineDataFetchRequest.getSourceUUID())
                .sourceType(sourceType)
                .connectionType(connectionType)
                .pipelineParam(pipelineSourceParam)
                .tableName(tableName)
                .lastSeenTime(lastSeenTime)
                .build();
        PipelineDataResponse response = pipelineFactory.getPipelineFetcher().fetchData(param);
        if (response != null) {
            try {
                PipelineDataResponseQueue.getInstance().put(response);
            } catch (InterruptedException e) {
                log.error("put response to queue error", e);
            }
        }
    }
}
