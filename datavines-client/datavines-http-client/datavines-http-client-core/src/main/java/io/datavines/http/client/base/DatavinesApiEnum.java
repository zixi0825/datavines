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
package io.datavines.http.client.base;


import com.fasterxml.jackson.core.type.TypeReference;
import io.datavines.http.client.request.UserLoginResult;
import io.datavines.http.client.response.DatavinesResponse;
import io.datavines.http.client.response.TaskResult;
import io.datavines.http.client.response.UserBaseInfo;
import io.datavines.http.client.response.WorkSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public enum DatavinesApiEnum {

    //TASK
    TASK_SUBMIT_API(new DatavinesAPI("/api/v1/task/submit", HttpMethod.POST, Response.Status.OK, new TypeReference<DatavinesResponse<Long>>() {})),
    TASK_KILL_API(new DatavinesAPI("/api/v1/task/kill/%s", HttpMethod.DELETE, Response.Status.OK, new TypeReference<DatavinesResponse<Long>>() {})),
    TASK_STATUS_API(new DatavinesAPI("/api/v1/task/status/%s", HttpMethod.GET, Response.Status.OK, new TypeReference<DatavinesResponse<String>>() {})),
    TASK_RESULT_API(new DatavinesAPI("/api/v1/task/result/%s", HttpMethod.GET, Response.Status.OK, new TypeReference<DatavinesResponse<TaskResult>>() {})),
    //METRIC
    METRIC_LIST_API(new DatavinesAPI("/api/v1/metric/list", HttpMethod.GET, Response.Status.OK, new TypeReference<DatavinesResponse<Set<String>>>() {})),
    METRIC_INFO_API(new DatavinesAPI("/api/v1/metric/info/%s", HttpMethod.GET, Response.Status.OK, new TypeReference<DatavinesResponse<HashMap<String, String>>>() {})),
    //WORKSPACE
    CREATE_WORKSPACE(new DatavinesAPI("/api/v1/workspace", HttpMethod.POST, Response.Status.OK, new TypeReference<DatavinesResponse<Long>>() {})),
    UPDATE_WORKSPACE(new DatavinesAPI("/api/v1/workspace", HttpMethod.PUT, Response.Status.OK, new TypeReference<DatavinesResponse<Integer>>() {})),
    DELETE_WORKSPACE(new DatavinesAPI("/api/v1/workspace/%s", HttpMethod.DELETE, Response.Status.OK, new TypeReference<DatavinesResponse<Integer>>() {})),
    LIST_WORKSPACE(new DatavinesAPI("/api/v1/workspace/list", HttpMethod.GET, Response.Status.OK, new TypeReference<DatavinesResponse<List<WorkSpace>>>() {})),
    //USER
    UPDATE_USER(new DatavinesAPI("/api/v1/user/update", HttpMethod.PUT, Response.Status.OK, new TypeReference<DatavinesResponse<UserLoginResult>>() {})),
    RESET_PASSWORD(new DatavinesAPI("/api/v1/user/resetPassword", HttpMethod.POST, Response.Status.OK, new TypeReference<DatavinesResponse<UserLoginResult>>() {})),
    LOGIN(new DatavinesAPI("/api/v1/login", HttpMethod.POST, Response.Status.OK, new TypeReference<DatavinesResponse<UserLoginResult>>() {})),
    REGISTER(new DatavinesAPI("/api/v1/register", HttpMethod.POST, Response.Status.OK, new TypeReference<DatavinesResponse<UserBaseInfo>>() {})),
    ;
    private Log log = LogFactory.getLog(DatavinesBaseClient.class);

    private final DatavinesAPI datavinesApi;

    DatavinesApiEnum(DatavinesAPI datavinesApi) {
        this.datavinesApi = datavinesApi;
    }

    public DatavinesAPI getDatavinesApi(String... path) {
        String formattedPath = String.format(datavinesApi.getPath(), path);
        return new DatavinesAPI(formattedPath, datavinesApi.getMethod(), datavinesApi.getExpectStatus(), getDatavinesApi().getResultType());
    }

    public DatavinesAPI getDatavinesApi() {
        if (datavinesApi.getPath().contains("%s")) {
            log.error(String.format("%s must be init with path", datavinesApi.getPath()));
        }
        return datavinesApi;
    }
}
