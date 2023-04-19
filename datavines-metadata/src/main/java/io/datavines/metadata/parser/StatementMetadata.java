package io.datavines.metadata.parser;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatementMetadata {

    private String statementText;

    private String statementIndex;

    private LocalDateTime statementParseStartTime;

    private LocalDateTime statementParseEndTime;

    private StatementMetadataFragment statementMetadataFragment;

}
