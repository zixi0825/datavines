package io.datavines.metadata.parser;

public interface StatementParser {

    StatementMetadataFragment parseStatement(String statement);
}
