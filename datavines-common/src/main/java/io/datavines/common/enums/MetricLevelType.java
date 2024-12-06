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
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;

public enum MetricLevelType {
    LEVEL1("1", "级别1"),
    LEVEL2("2", "级别2"),
    LEVEL3("3", "级别3"),
    LEVEL4("4", "级别4"),
    LEVEL5("5", "级别5"),
    ;

    @JsonValue
    @EnumValue
    @Getter
    private final String code;

    @Getter
    private final String description;

    MetricLevelType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final HashMap<String, MetricLevelType> MAP = new HashMap<>();

    static {
        for (MetricLevelType type: MetricLevelType.values()){
            MAP.put(type.getCode(), type);
        }
    }

    public static MetricLevelType of(String code){
        if (MAP.containsKey(code)){
            return MAP.get(code);
        }
        throw new IllegalArgumentException("invalid code : " + code);
    }
}
