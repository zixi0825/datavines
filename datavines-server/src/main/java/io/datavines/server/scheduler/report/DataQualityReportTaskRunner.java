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
package io.datavines.server.scheduler.report;

import io.datavines.common.enums.ExecutionStatus;
import io.datavines.server.repository.service.JobQualityReportService;
import io.datavines.server.scheduler.CommonTaskContext;
import io.datavines.server.scheduler.CommonTaskResponse;
import io.datavines.server.scheduler.CommonTaskResponseQueue;
import io.datavines.server.utils.SpringApplicationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataQualityReportTaskRunner implements Runnable {

    private final CommonTaskContext taskContext;

    private final CommonTaskResponseQueue responseQueue =
            SpringApplicationContext.getBean(CommonTaskResponseQueue.class);

    public DataQualityReportTaskRunner(CommonTaskContext taskContext) {
        this.taskContext = taskContext;
    }

    @Override
    public void run() {
        try {
            JobQualityReportService jobQualityReportService = SpringApplicationContext.getBean(JobQualityReportService.class);
            jobQualityReportService.generateQualityReport(taskContext.getCommonTaskRequest().getDataSource().getId());
            log.info("data quality report generate finished");
            responseQueue.add(new CommonTaskResponse(taskContext.getCatalogTaskId(), ExecutionStatus.SUCCESS.getCode()));
        } catch (Exception e) {
            log.error("data quality report generate error: ", e);
            responseQueue.add(new CommonTaskResponse(taskContext.getCatalogTaskId(), ExecutionStatus.FAILURE.getCode()));
        }
    }
}
