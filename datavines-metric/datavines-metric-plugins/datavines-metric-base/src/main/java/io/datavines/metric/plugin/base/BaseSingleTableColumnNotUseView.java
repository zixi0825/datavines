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
package io.datavines.metric.plugin.base;

import io.datavines.common.entity.ExecuteSql;
import io.datavines.metric.api.ConfigItem;
import io.datavines.metric.api.MetricLevel;

import java.util.Map;

import static io.datavines.common.ConfigConstants.METRIC_UNIQUE_KEY;

public abstract class BaseSingleTableColumnNotUseView extends BaseSingleTable {

    public BaseSingleTableColumnNotUseView() {
        super();
        configMap.put("column",new ConfigItem("column", "列名", "column"));
        requiredOptions.add("column");
    }

    @Override
    public Map<String, ConfigItem> getConfigMap() {
        return configMap;
    }

    @Override
    public MetricLevel getLevel() {
        return MetricLevel.COLUMN;
    }

    @Override
    public boolean isInvalidateItemsCanOutput() {
        return false;
    }

    @Override
    public ExecuteSql getDirectActualValue(Map<String, String> inputParameter) {
        ExecuteSql executeSql = new ExecuteSql();
        String uniqueKey = inputParameter.get(METRIC_UNIQUE_KEY);
        executeSql.setResultTable("invalidate_count_" + uniqueKey);
        String actualValueSql = "select count(1) as actual_value_"+ uniqueKey +" from ( " + invalidateItemsSql.toString() + " ) t";
        executeSql.setSql(actualValueSql);
        executeSql.setErrorOutput(false);
        return executeSql;
    }
}
