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
package io.datavines.server.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskExecutePlatformType {
    /**
     * 资源调度平台类型
     */
    LOCAL(0,"local"),
    YARN(1,"yarn"),
    K8S(2,"k8s");

    TaskExecutePlatformType(int code, String description){
        this.code = code;
        this.description = description;
    }

    @EnumValue
    @JsonValue
    private final int code;

    private final String description;

    public static TaskExecutePlatformType of(int code){
        for(TaskExecutePlatformType taskExecutePlatformType : values()){
            if(taskExecutePlatformType.getCode() == code){
                return taskExecutePlatformType;
            }
        }
        throw new IllegalArgumentException("invalid type : " + code);
    }

    public static TaskExecutePlatformType of(String description){
        for(TaskExecutePlatformType taskExecutePlatformType : values()){
            if(taskExecutePlatformType.getDescription().equals(description)){
                return taskExecutePlatformType;
            }
        }
        throw new IllegalArgumentException("invalid type : " + description);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
