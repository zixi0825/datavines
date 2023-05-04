package io.datavines.pipeline.scheduler;

import io.datavines.common.utils.NamedThreadFactory;
import io.datavines.common.utils.Stopper;
import io.datavines.common.utils.ThreadUtils;
import io.datavines.pipeline.api.PipelineDataResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.datavines.common.CommonConstants.SLEEP_TIME_MILLIS;

@Slf4j
public class PipelineDataResponseOperator {

    // data response queue
    private PipelineDataResponseQueue dataResponseQueue;

    // 数据处理调度线程，不断轮循取出数据，丢进数据处理线程进行处理
    private final ExecutorService executorService;

    public PipelineDataResponseOperator(PipelineDataResponseQueue dataResponseQueue) {
        this.executorService = Executors.newFixedThreadPool(5, new NamedThreadFactory("Server-thread"));
        this.dataResponseQueue = dataResponseQueue;
    }

    class ResponseOperateThread extends Thread {

        @Override
        public void run() {
            while (Stopper.isRunning()) {
                try {
                    PipelineDataResponse response = dataResponseQueue.take();
                    executorService.execute(new PipelineDataResponseOperateTask(response));
                    ThreadUtils.sleep(SLEEP_TIME_MILLIS);
                } catch (Exception e) {
                    log.info("interrupted when take data response from queue");
                }
            }
        }
    }
}
