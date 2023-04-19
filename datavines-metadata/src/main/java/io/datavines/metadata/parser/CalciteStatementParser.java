package io.datavines.metadata.parser;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

public class CalciteStatementParser implements StatementParser {

    private final SqlParser.Config calciteConfig  = SqlParser.config()
            .withParserFactory(SqlDdlParserImpl.FACTORY)
            .withConformance(SqlConformanceEnum.BABEL);
    @Override
    public StatementMetadataFragment parseStatement(String statement) {

        StatementMetadataFragment statementMetadataFragment = null;
        try {
            SqlParser parser = SqlParser.create(statement, calciteConfig);
            SqlNode stmt = parser.parseStmt();
            Visitor visitor = new Visitor();
            stmt.accept(visitor);
            statementMetadataFragment = visitor.buildMetadata(statement);
        } catch (SqlParseException e) {
            throw new RuntimeException(e);
        }

        return statementMetadataFragment;
    }


    public static void main(String[] args) {
        CalciteStatementParser calciteStatementParser = new CalciteStatementParser();
        StatementMetadataFragment statementMetadataFragment = calciteStatementParser.parseStatement("SELECT a,b FROM dw.some_table");
        System.out.println(statementMetadataFragment);

    }
}
