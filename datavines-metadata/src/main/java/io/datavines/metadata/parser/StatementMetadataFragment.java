package io.datavines.metadata.parser;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatementMetadataFragment {

    private String statementParser;

    private String statementType;

    private List<String> inputObjects;

    private List<String> outputObjects;

}
