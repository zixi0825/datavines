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

public enum MetricCategoryType {
    BASIC("basic", "基础规则"),
    TEMPLATE("template", "规则模版"),
    ;

    @JsonValue
    @EnumValue
    @Getter
    private final String code;

    @Getter
    private final String description;

    MetricCategoryType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final HashMap<String, MetricCategoryType> METRIC_CATEGORY_TYPE_MAP = new HashMap<>();

    static {
        for (MetricCategoryType categoryType: MetricCategoryType.values()){
            METRIC_CATEGORY_TYPE_MAP.put(categoryType.getCode(), categoryType);
        }
    }

    public static MetricCategoryType of(String code){
        if (METRIC_CATEGORY_TYPE_MAP.containsKey(code)){
            return METRIC_CATEGORY_TYPE_MAP.get(code);
        }
        throw new IllegalArgumentException("invalid code : " + code);
    }
}
