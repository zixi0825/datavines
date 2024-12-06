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
package io.datavines.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.HashMap;

/**
 * job type
 */
public enum IssueStatusType {
    /**
     * wait_process
     * processed
     * ignored
     */
    WAIT_PROCESS("wait_process", "WAIT_PROCESS", "待处理"),
    PROCESSED("processed", "PROCESSED","已处理"),
    IGNORED("ignored", "IGNORED","已忽略");

    IssueStatusType(String code, String description, String zhDescription) {
        this.code = code;
        this.description = description;
        this.zhDescription = zhDescription;
    }

    @EnumValue
    private final String code;

    private final String description;

    private final String zhDescription;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getZhDescription() {
        return zhDescription;
    }

    private static final HashMap<String, IssueStatusType> JOB_TYPE_MAP = new HashMap<>();

    static {
        for (IssueStatusType jobType: IssueStatusType.values()){
            JOB_TYPE_MAP.put(jobType.code, jobType);
        }
    }

    public static IssueStatusType of(String jobType){
        if(JOB_TYPE_MAP.containsKey(jobType)){
            return JOB_TYPE_MAP.get(jobType);
        }
        throw new IllegalArgumentException("invalid job type : " + jobType);
    }
}
