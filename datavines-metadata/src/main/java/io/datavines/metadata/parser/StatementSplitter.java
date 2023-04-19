package io.datavines.metadata.parser;

import java.util.List;

public interface StatementSplitter {

    List<String> splitStatements(String body) ;
}
