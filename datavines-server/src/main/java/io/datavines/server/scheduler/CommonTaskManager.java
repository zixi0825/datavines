/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.datavines.server.scheduler;

import io.datavines.common.utils.*;
import io.datavines.server.enums.FetchType;
import io.datavines.server.scheduler.metadata.CatalogMetaDataFetchTaskRunner;
import io.datavines.server.repository.entity.DataSource;
import io.datavines.server.repository.entity.CommonTask;
import io.datavines.server.repository.service.CommonTaskService;
import io.datavines.server.repository.service.impl.JobExternalService;
import io.datavines.server.scheduler.report.DataQualityReportTaskRunner;
import io.datavines.server.utils.NamedThreadFactory;
import io.datavines.server.utils.SpringApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class CommonTaskManager {

    private final LinkedBlockingQueue<CommonTaskContext> taskQueue = new LinkedBlockingQueue<>();

    private final CommonTaskResponseQueue responseQueue =
            SpringApplicationContext.getBean(CommonTaskResponseQueue.class);

    private final CommonTaskService commonTaskService =
            SpringApplicationContext.getBean(CommonTaskService.class);

    private final JobExternalService jobExternalService =
            SpringApplicationContext.getBean(JobExternalService.class);

    private final ExecutorService taskExecuteService;

    public CommonTaskManager() {
        this.taskExecuteService = Executors.newFixedThreadPool(
                CommonPropertyUtils.getInt(CommonPropertyUtils.METADATA_FETCH_EXEC_THREADS,CommonPropertyUtils.METADATA_FETCH_EXEC_THREADS_DEFAULT),
                new NamedThreadFactory("CommonTaskExecutor-Execute-Thread"));
    }

    public void start() {
        new TaskExecutor().start();

        new TaskResponseOperator().start();
    }

    class TaskExecutor extends Thread {

        @Override
        public void run() {
            while(Stopper.isRunning()) {
                try {
                    CommonTaskContext commonTaskContext = taskQueue.take();
                    switch (commonTaskContext.getCommonTaskType()) {
                        case CATALOG_METADATA_FETCH:
                            taskExecuteService.execute(new CatalogMetaDataFetchTaskRunner(commonTaskContext));
                            break;
                        case DATA_QUALITY_REPORT:
                            taskExecuteService.execute(new DataQualityReportTaskRunner(commonTaskContext));
                            break;
                        default:
                            break;
                    }

                    CommonTask commonTask = commonTaskService.getById(commonTaskContext.getCatalogTaskId());
                    if (commonTask != null) {
                        commonTask.setStartTime(LocalDateTime.now());
                        commonTaskService.update(commonTask);
                    }
                    ThreadUtils.sleep(1000);
                } catch(Exception e) {
                    log.error("dispatcher catalog task error",e);
                    ThreadUtils.sleep(2000);
                }
            }
        }
    }

    /**
     * operate task response
     */
    class TaskResponseOperator extends Thread {

        @Override
        public void run() {
            while (Stopper.isRunning()) {
                try {
                    CommonTaskResponse taskResponse = responseQueue.take();
                    log.info("CatalogTaskResponse: " + JSONUtils.toJsonString(taskResponse));
                    CommonTask commonTask = commonTaskService.getById(taskResponse.getCatalogTaskId());
                    if (commonTask != null) {
                        commonTask.setStatus(taskResponse.getStatus());
                        commonTask.setEndTime(LocalDateTime.now());
                        commonTaskService.update(commonTask);
                    }
                    ThreadUtils.sleep(1000);
                } catch(Exception e) {
                    log.error("operate catalog task response error", e);
                }
            }
        }
    }

    public void putCommonTask(CommonTask commonTask) throws InterruptedException {
        if (commonTask == null) {
            return;
        }

        Long dataSourceId = commonTask.getDataSourceId();
        DataSource dataSource = jobExternalService.getDataSourceService().getDataSourceById(dataSourceId);
        if (dataSource == null) {
            return;
        }

        CommonTaskRequest commonTaskRequest = new CommonTaskRequest();
        commonTaskRequest.setDataSource(dataSource);
        commonTaskRequest.setFetchType(FetchType.DATASOURCE);

        String parameter = commonTask.getParameter();
        if (StringUtils.isNotEmpty(parameter)) {
            Map<String, String> parameterMap = JSONUtils.toMap(parameter);
            if (parameterMap != null) {
                String database = parameterMap.get("database");
                if (StringUtils.isNotEmpty(database)) {
                    commonTaskRequest.setDatabase(database);
                    commonTaskRequest.setFetchType(FetchType.DATABASE);
                }

                String table = parameterMap.get("table");
                if (StringUtils.isNotEmpty(table)) {
                    commonTaskRequest.setTable(table);
                    commonTaskRequest.setFetchType(FetchType.TABLE);
                }
            }
        }

        CommonTaskContext commonTaskContext = new CommonTaskContext(commonTask.getTaskType(), commonTaskRequest, commonTask.getId());
        taskQueue.put(commonTaskContext);
    }
}
