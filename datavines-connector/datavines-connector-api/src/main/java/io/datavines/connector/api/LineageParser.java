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
package io.datavines.connector.api;

import io.datavines.common.utils.StringUtils;
import io.datavines.connector.api.entity.ScriptMetadata;
import io.datavines.connector.api.entity.StatementMetadata;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class LineageParser {

    public static ScriptMetadata parseScript(String script, StatementSplitter statementSplitter, StatementParser  statementParser) {
        if (StringUtils.isEmpty(script)) {
            return null;
        }

        ScriptMetadata scriptMetadata = new ScriptMetadata();
        scriptMetadata.setScript(script);
        List<String> statements = statementSplitter.splitStatements(script);

        if (CollectionUtils.isEmpty(statements)) {
            return null;
        }

        for (int i=0; i<statements.size(); i++) {
            StatementMetadata statementMetadata = new StatementMetadata();
            statementMetadata.setStatementIndex(i);
            statementMetadata.setStatementText(statements.get(i));
            statementMetadata.setStatementParseStartTime(LocalDateTime.now());
            statementMetadata.setStatementMetadataFragment(statementParser.parseStatement(statements.get(i)));
            statementMetadata.setStatementParseEndTime(LocalDateTime.now());
            scriptMetadata.addStatementMetadata(statementMetadata);
        }

        return scriptMetadata;
    }

}
