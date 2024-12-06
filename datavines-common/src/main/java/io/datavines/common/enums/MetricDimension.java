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

public enum MetricDimension {
    /**
     * 0-completeness 完整性
     * 1-consistency 一致性
     * 2-effectiveness 有效性
     * 3-timeliness 及时性
     * 4-uniqueness 唯一性
     * 5-accuracy   准确性
     */
    COMPLETENESS("completeness","完整性"),
    CONSISTENCY("consistency","一致性"),
    EFFECTIVENESS("effectiveness","有效性"),
    TIMELINESS("timeliness","及时性"),
    UNIQUENESS("uniqueness","唯一性"),
    ACCURACY("accuracy","准确性");

    MetricDimension(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    @EnumValue
    @Getter
    private final String code;

    @Getter
    private final String description;

    private static final HashMap<String, MetricDimension> MAP = new HashMap<>();

    static {
        for (MetricDimension type: MetricDimension.values()){
            MAP.put(type.getCode(), type);
        }
    }

    public static MetricDimension of(String code){
        if (MAP.containsKey(code)){
            return MAP.get(code);
        }
        throw new IllegalArgumentException("invalid code : " + code);
    }
}