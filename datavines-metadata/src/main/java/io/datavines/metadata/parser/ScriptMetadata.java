package io.datavines.metadata.parser;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScriptMetadata {

    private String scriptName;

    private String scriptSource;

    private String scriptKind;

    private String scriptParser;

    private LocalDateTime scriptParseStartTime;

    private LocalDateTime scriptParseEndTime;

    private List<StatementMetadata> statementsMetadataList;

}