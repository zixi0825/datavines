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

import io.datavines.common.utils.CommonPropertyUtils;
import io.datavines.common.utils.NetUtils;
import io.datavines.server.repository.entity.CommonTask;
import io.datavines.server.repository.service.CommonTaskService;
import io.datavines.server.utils.SpringApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Slf4j
public class CommonTaskFailover {

    private final CommonTaskService commonTaskService;

    private final CommonTaskManager commonTaskManager;

    public CommonTaskFailover(CommonTaskManager commonTaskManager) {
        this.commonTaskService = SpringApplicationContext.getBean(CommonTaskService.class);
        this.commonTaskManager = commonTaskManager;
    }

    public void handleMetaDataFetchTaskFailover(String host) {
        List<CommonTask> needFailoverTaskList = commonTaskService.listNeedFailover(host);
        innerHandleMetaDataFetchTaskFailover(needFailoverTaskList);
    }

    private void innerHandleMetaDataFetchTaskFailover(List<CommonTask> needFailover) {
        if (CollectionUtils.isNotEmpty(needFailover)) {
            needFailover.forEach(task -> {
                task.setExecuteHost(NetUtils.getAddr(
                        CommonPropertyUtils.getInt(CommonPropertyUtils.SERVER_PORT, CommonPropertyUtils.SERVER_PORT_DEFAULT)));
                commonTaskService.updateById(task);

                try {
                    commonTaskManager.putCommonTask(task);
                } catch (Exception e) {
                    log.error("put the task need failover into manager error : ", e);
                }
            });
        }
    }

    public void handleMetaDataFetchTaskFailover(List<String> hostList) {
        List<CommonTask> needFailoverTaskList = commonTaskService.listTaskNotInServerList(hostList);
        innerHandleMetaDataFetchTaskFailover(needFailoverTaskList);
    }
}
