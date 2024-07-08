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
package io.datavines.http.client;

import io.datavines.http.client.base.DatavinesApiEnum;
import io.datavines.http.client.base.DatavinesApiException;
import io.datavines.http.client.base.DatavinesBaseClient;
import io.datavines.http.client.request.*;
import io.datavines.http.client.response.DatavinesResponse;
import io.datavines.http.client.response.UserBaseInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

public class DataVinesClient extends DatavinesBaseClient {

    private Log log = LogFactory.getLog(DataVinesClient.class);

    public DataVinesClient(String baseUrl){
        super(baseUrl, new Properties(), "test", null);
    }

    /**
     * submit task
     * @param params
     * @return
     */
    public DatavinesResponse submitTask(String params) throws DatavinesApiException {
        DatavinesResponse res = callAPI(DatavinesApiEnum.TASK_SUBMIT_API.getDatavinesApi(), params);
        return res;
    }

    /**
     * submit task
     * @param request
     * @return
     */
    public DatavinesResponse submitTask(SubmitTaskRequest request) throws DatavinesApiException {
        DatavinesResponse res = callAPI(DatavinesApiEnum.TASK_SUBMIT_API.getDatavinesApi(), request);
        return res;
    }

    /**
     * kill task
     * @param taskId
     * @return
     * @throws DatavinesApiException
     */
    public DatavinesResponse killTask(Long taskId) throws DatavinesApiException {
       return callApiWithPathParam(DatavinesApiEnum.TASK_KILL_API, taskId);
    }

    /**
     * get task status
     * @param taskId
     * @return task status
     * @throws DatavinesApiException
     */
    public DatavinesResponse taskStatus(Long taskId) throws DatavinesApiException {
        return callApiWithPathParam(DatavinesApiEnum.TASK_STATUS_API, taskId);
    }

    /**
     * get task result info
     * @param taskId
     * @return task result
     * @throws DatavinesApiException
     */
    public DatavinesResponse taskResultInfo(Long taskId) throws DatavinesApiException {
        return callApiWithPathParam(DatavinesApiEnum.TASK_RESULT_API, taskId);
    }

    /**
     * get metric list
     * @return metric list
     * @throws DatavinesApiException
     */
    public DatavinesResponse metricList() throws DatavinesApiException {
        return callAPI(DatavinesApiEnum.METRIC_LIST_API);
    }

    /**
     * get metric info by metric name
     * @param name metric name
     * @return metric info
     * @throws DatavinesApiException
     */
    public DatavinesResponse metricInfo(String name) throws DatavinesApiException {
        return callApiWithPathParam(DatavinesApiEnum.METRIC_INFO_API, name);
    }

    public DatavinesResponse createWorkSpace(String spaceName) throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.CREATE_WORKSPACE.getDatavinesApi(), new WorkSpaceCreateRequest(spaceName));
        return result;
    }

    public DatavinesResponse createWorkSpace(WorkSpaceCreateRequest spaceName) throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.CREATE_WORKSPACE.getDatavinesApi(), spaceName);
        return result;
    }

    public DatavinesResponse updateWorkSpace(WorkSpaceUpdateRequest update) throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.UPDATE_WORKSPACE.getDatavinesApi(), update);
        return result;
    }

    public DatavinesResponse deleteWorkSpace(Long workspaceId) throws DatavinesApiException {
        DatavinesResponse result = callApiWithPathParam(DatavinesApiEnum.DELETE_WORKSPACE, workspaceId);
        return result;
    }

    public DatavinesResponse listWorkSpace() throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.LIST_WORKSPACE);
        return result;
    }

    public DatavinesResponse register(UserRegisterRequest registerRequest) throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.REGISTER.getDatavinesApi(), registerRequest);
        return result;
    }

    public DatavinesResponse login(UserLoginRequest loginRequest) throws DatavinesApiException {
        DatavinesResponse result = callAPI(DatavinesApiEnum.LOGIN.getDatavinesApi(), loginRequest);
        return result;
    }

    public DatavinesResponse<UserBaseInfo> login(String name, String password) throws DatavinesApiException {
        DatavinesResponse<UserBaseInfo> result = callAPI(DatavinesApiEnum.LOGIN.getDatavinesApi(), new UserLoginRequest(name, password));
        return result;
    }

    private <T> DatavinesResponse<T> callApiWithPathParam(DatavinesApiEnum dataVinesAPI, Serializable id) throws DatavinesApiException {
        if (Objects.isNull(id)){
            log.error("task id must not null!");
            throw new DatavinesApiException("task id must not null!");
        }
        DatavinesResponse result = callAPI(dataVinesAPI.getDatavinesApi(String.valueOf(id)), null);
        return result;
    }

    private <T> DatavinesResponse<T> callAPI(DatavinesApiEnum dataVinesAPI) throws DatavinesApiException {
        DatavinesResponse result = callAPI(dataVinesAPI.getDatavinesApi(), null);
        return result;
    }
}
