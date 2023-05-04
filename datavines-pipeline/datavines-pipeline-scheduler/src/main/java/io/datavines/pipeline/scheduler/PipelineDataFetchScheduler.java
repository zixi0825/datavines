package io.datavines.pipeline.scheduler;

import io.datavines.common.utils.*;
import io.datavines.core.registry.Register;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.datavines.common.CommonConstants.SLEEP_TIME_MILLIS;
import static io.datavines.common.utils.CommonPropertyUtils.*;
import static io.datavines.common.utils.CommonPropertyUtils.RESERVED_MEMORY_DEFAULT;

@Slf4j
public class PipelineDataFetchScheduler extends Thread {

    //-添加pipeline服务配置信息，点击启动同步数据、按照一定节奏进行数据同步
    //--记录每个pipeline服务中每张表的上次同步时间
    //-数据同步逻辑
    //--启动时判断是否存在需要进行同步的Pipeline服务，如果存在则启动同步，同时获取同步时间，如果存在则获取上次同步时间后的数据，否则进行全量同步
    //--同步数据时，根据pipeline服务配置信息，获取需要同步的表，根据上次同步时间获取需要同步的数据，进行同步
    //--同步完成后，更新pipeline服务配置信息中每张表的同步时间
    //--同步完成后，向同步命令表中插入同步命令、pipeline服务ID、期望执行时间

    private final String PIPELINE_FETCH_TASK_LOCK_KEY =
            CommonPropertyUtils.getString(CommonPropertyUtils.PIPELINE_FETCH_TASK_LOCK_KEY, CommonPropertyUtils.PIPELINE_FETCH_TASK_LOCK_KEY_DEFAULT);

    private static final int[] RETRY_BACKOFF = {1, 2, 3, 5, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};

    private final Register register;

    private final PipelineDataFetchRequestQueue requestQueue = PipelineDataFetchRequestQueue.getInstance();

    private final ExecutorService executorService;

    public PipelineDataFetchScheduler(Register register){
        this.register = register;
        this.executorService = Executors.newFixedThreadPool(10, new NamedThreadFactory("Pipeline-Executor"));
    }

    @Override
    public void run() {
        log.info("pipeline data fetch task scheduler started");

        int retryNum = 0;
        while (Stopper.isRunning()) {
            PipelineDataFetchRequest fetchRequest = null;
            try {
                register.blockUtilAcquireLock(PIPELINE_FETCH_TASK_LOCK_KEY);

                fetchRequest = requestQueue.take();

                if (fetchRequest != null) {
                    executorService.execute(new PipelineDataFetchTask(fetchRequest));
                    register.release(PIPELINE_FETCH_TASK_LOCK_KEY);
                    ThreadUtils.sleep(SLEEP_TIME_MILLIS);
                } else {
                    register.release(PIPELINE_FETCH_TASK_LOCK_KEY);
                    ThreadUtils.sleep(SLEEP_TIME_MILLIS * 2);
                }

                retryNum = 0;
            } catch (Exception e){
                retryNum++;
                log.error("schedule pipeline data fetch task error ", e);
                ThreadUtils.sleep(SLEEP_TIME_MILLIS * RETRY_BACKOFF [retryNum % RETRY_BACKOFF.length]);
            } finally {
                register.release(PIPELINE_FETCH_TASK_LOCK_KEY);
            }
        }
    }
}
