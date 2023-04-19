package io.datavines.metadata.parser;

import io.datavines.common.utils.StringUtils;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlCreateTable;
import org.apache.calcite.sql.ddl.SqlCreateView;
import org.apache.calcite.sql.util.SqlBasicVisitor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

import static org.apache.calcite.sql.SqlKind.AS;

public class Visitor extends SqlBasicVisitor<Void> {
    private String statementType;
    private Set<String> inputTables = new HashSet<>();
    private Set<String> outputTables = new HashSet<>();;
    private Set<Throwable> errors = new HashSet<>();;
    private Map<String,String> aliasTableMapping = new HashMap<>();

    private LinkedHashSet<String> allAlias = new LinkedHashSet<>();

    private String lastVisitedInputTables = "";
    private Map<String,Set<String>> tableColumnMapping = new HashMap<>();

    private Map<String,SqlSelect> aliasMapping = new HashMap<>();

    public StatementMetadataFragment buildMetadata(String statement) {
        System.out.println("****");
        System.out.println(statement);
        System.out.println("inputTables" + inputTables);
        System.out.println("outputTables" + outputTables);
        System.out.println("allAlias" + allAlias);
        System.out.println("columntableMapping" + tableColumnMapping);
        System.out.println("aliasMapping" + aliasMapping);
        System.out.println("****");
        return new StatementMetadataFragment(
                getClass().getName(),
                statementType,
                new ArrayList<>(inputTables),
                new ArrayList<>(outputTables));

    }

    @Override
    public Void visit(SqlCall call) {
        if (StringUtils.isEmpty(statementType)) {
            statementType = call.getKind().toString();
        }

        switch (call.getKind()) {
            case CREATE_TABLE:
                visitCreateTable((SqlCreateTable) call);
                break;
            case CREATE_VIEW:
                visitCreateView((SqlCreateView)call);
                break;
            case INSERT:
                visitInsert((SqlInsert)call);
                break;
            case SELECT:
                visitSelect((SqlSelect) call);
                break;
            case UPDATE:
                visitUpdate((SqlUpdate)call);
                break;
            case MERGE:
                visitMerge((SqlMerge)call);
                break;
            case ORDER_BY:
                SqlOrderBy orderBy = (SqlOrderBy)call;
                if (orderBy.query instanceof SqlSelect) {
                    visitSelect((SqlSelect)orderBy.query);
                }

                if (orderBy.orderList != null) {

                }

            default:
                break;
        }

        return super.visit(call);
    }

    private void visitCreateTable(SqlCreateTable createTable) {
        String tableName = "";
        SqlNode tableNameNode = createTable.getOperandList().get(0);
        if (tableNameNode instanceof SqlIdentifier) {
            visitOutputIdentifier(createTable.name);
            tableName = createTable.name.toString();
        }

        SqlNode columnListNode = createTable.getOperandList().get(1);
        if (columnListNode instanceof SqlNodeList) {
            SqlNodeList columnList = (SqlNodeList)columnListNode;
            if (CollectionUtils.isEmpty(columnList.getList())) {
                return;
            }
            for (SqlNode columnNode : columnList.getList()) {
                if (columnNode instanceof SqlColumnDeclaration) {
                    SqlColumnDeclaration sqlColumnDeclaration = (SqlColumnDeclaration)columnNode;
                    if (sqlColumnDeclaration.getOperandList().get(0) instanceof SqlIdentifier) {
                        SqlIdentifier sqlIdentifier = (SqlIdentifier)sqlColumnDeclaration.getOperandList().get(0);
                        visitSqlIdentifier(sqlIdentifier,tableName);
                    }
                }
            }
        }
    }

    private void visitCreateView(SqlCreateView createView){
        // Visit the name of the view being created
        String tableName = "";
        SqlNode tableNameNode = createView.getOperandList().get(0);
        if (tableNameNode instanceof SqlIdentifier) {
            visitOutputIdentifier(createView.name);
            tableName = createView.name.toString();
        }

        SqlNode columnListNode = createView.getOperandList().get(1);
        if (columnListNode instanceof SqlNodeList) {
            SqlNodeList columnList = (SqlNodeList)columnListNode;
            if (CollectionUtils.isEmpty(columnList.getList())) {
                return;
            }
            for (SqlNode columnNode : columnList.getList()) {
                if (columnNode instanceof SqlColumnDeclaration) {
                    SqlColumnDeclaration sqlColumnDeclaration = (SqlColumnDeclaration)columnNode;
                    if (sqlColumnDeclaration.getOperandList().get(0) instanceof SqlIdentifier) {
                        SqlIdentifier sqlIdentifier = (SqlIdentifier)sqlColumnDeclaration.getOperandList().get(0);
                        visitSqlIdentifier(sqlIdentifier,tableName);
                    }
                }
            }
        }
    }

    private void visitInsert(SqlInsert insert) {

        String tableName = "";
        // Visit "source" node of insert statement
        if (insert.getSource() instanceof SqlIdentifier) {
            visitInputIdentifier((SqlIdentifier)insert.getSource());
        }

        if (insert.getTargetTable() instanceof SqlIdentifier) {
            SqlIdentifier id = (SqlIdentifier)insert.getTargetTable();
            visitOutputIdentifier((SqlIdentifier)insert.getTargetTable());
            tableName = id.toString();
        }

        if (insert.getTargetColumnList() != null) {
            visitSQLNodeList(insert.getTargetColumnList(), tableName);
        }
    }

    private void visitSQLNodeList(SqlNodeList nodeList,String tableName) {

        if (CollectionUtils.isEmpty(nodeList.getList())) {
            return;
        }

        for (SqlNode sqlNode : nodeList.getList()) {
            if (sqlNode instanceof SqlIdentifier) {
                visitSqlIdentifier((SqlIdentifier)sqlNode, tableName);
            }
        }
    }

    private void visitSelect(SqlSelect select) {

        String tableName = "";
        if (select.getFrom() instanceof SqlIdentifier) {
            SqlIdentifier id = (SqlIdentifier) select.getFrom();
            visitInputIdentifier(id);
            tableName = id.toString();
        }

        if (select.getFrom() instanceof SqlBasicCall) {
            SqlBasicCall as = (SqlBasicCall) select.getFrom();
            if (as.getKind() == AS) {
                visitAs(as);
            }
        }

        if (select.getFrom() instanceof SqlJoin) {
            visitJoin((SqlJoin) select.getFrom(), tableName);
        }


        // Helper for visiting a Join statement inside of a Select statement
        SqlNode where = select.getWhere();
        if (where instanceof SqlBasicCall) {
            SqlBasicCall basicCall = (SqlBasicCall) where;
            List<SqlNode> clauses = basicCall.getOperandList();
            if (CollectionUtils.isEmpty(clauses)) {
                return;
            }

            for (SqlNode clause : clauses) {
                if (clause instanceof SqlBasicCall) {
                    SqlBasicCall clauseNode = (SqlBasicCall) clause;
                    if (clauseNode.getOperandList().get(0) instanceof SqlIdentifier) {
                        visitSqlIdentifier((SqlIdentifier) clauseNode.getOperandList().get(0), tableName);
                    }
                }

                if (clause instanceof SqlIdentifier) {
                    visitSqlIdentifier((SqlIdentifier) clause, tableName);
                }
            }
        }

        if (select.getSelectList() != null) {
            SqlNodeList nodeList = select.getSelectList();
            List<SqlNode> nodes = nodeList.getList();
            if (StringUtils.isEmpty(tableName)) {
                if (!allAlias.isEmpty()) {
                    // get last one
                    List<String> aliasList = new ArrayList<>(allAlias);
                    tableName = aliasList.get(aliasList.size() - 1);
                }
            }

            for (SqlNode node : nodes) {
                if (node instanceof SqlIdentifier) {
                    visitSqlIdentifier((SqlIdentifier) node, tableName);
                }

                if (node instanceof SqlBasicCall) {
                    SqlBasicCall sqlBasicCall = (SqlBasicCall) node;
                    if (AS == sqlBasicCall.getKind()) {
                        SqlNode columnNode = sqlBasicCall.getOperandList().get(0);
                        SqlNode aliasNode = sqlBasicCall.getOperandList().get(1);

                        if (columnNode instanceof SqlIdentifier) {
                            visitSqlIdentifier((SqlIdentifier) columnNode, tableName);
                        }

                        if (aliasNode instanceof SqlIdentifier) {
                            SqlIdentifier id = (SqlIdentifier) aliasNode;
                            allAlias.add(id.toString());
                        }
                    }
                }
            }
        }
    }

    // Helper for visiting a Join statement inside of a Select statement
    private void visitJoin(SqlJoin join, String tableName) {
        List<SqlNode> list = new ArrayList<>();
        list.add(join.getLeft());
        list.add(join.getRight());
        list.add(join.getCondition());

        list.forEach(node -> {
            if (node instanceof SqlIdentifier) {
                SqlIdentifier id = (SqlIdentifier)node;
                visitInputIdentifier(id);
                if (id.names.size() < 2) {
                    // if no alias
                    aliasTableMapping.put(id.names.get(0), id.names.get(0));
                } else {
                    // if alias
                    aliasTableMapping.put(id.names.get(1), id.names.get(0));
                }
            }

            if (node instanceof SqlBasicCall) {
                SqlBasicCall basicCall = (SqlBasicCall)node;
                if (basicCall.getKind() == AS) {
                    visitAs(basicCall);
                } else {
                    List<SqlNode> clauses = basicCall.getOperandList();
                    if (CollectionUtils.isEmpty(clauses)) {
                        return;
                    }

                    for (SqlNode clause : clauses) {
                        if (clause instanceof SqlBasicCall) {
                            SqlBasicCall clauseNode = (SqlBasicCall)clause;
                            if (clauseNode.getOperandList().get(0) instanceof SqlIdentifier) {
                                visitSqlIdentifier((SqlIdentifier) clauseNode.getOperandList().get(0),tableName);
                            }
                        }

                        if (clause instanceof SqlIdentifier) {
                            visitSqlIdentifier((SqlIdentifier)clause, tableName);
                        }
                    }
                }
            }

            if (node instanceof SqlJoin) {
                visitJoin((SqlJoin)node, tableName);
            }
        });
    }

    // Visit an AS statement inside a SELECT statement
    private void visitAs(SqlBasicCall basic) {
        // Each AS statement has two operands. We only care about the first (second is an alias)

        String tableName = "";
        SqlNode importantNode = basic.getOperandList().get(0);
        SqlNode aliasNode = basic.getOperandList().get(1);

        if (importantNode instanceof SqlIdentifier) {
            SqlIdentifier id = (SqlIdentifier)importantNode;
            visitInputIdentifier(id);
            tableName = id.toString();
        }

        if (aliasNode instanceof SqlIdentifier) {
            SqlIdentifier id = (SqlIdentifier)aliasNode;
            if(StringUtils.isNotEmpty(tableName)){
                aliasTableMapping.put(id.toString(), tableName);
            } else if(basic.getOperandList().get(0) instanceof SqlSelect){
                aliasMapping.put(id.toString(), (SqlSelect)basic.getOperandList().get(0));
            }
            allAlias.add(id.toString());
        }
    }
    /**
     * In an UPDATE statement, we record the name of the target table
     * as an output table.
     */
    private void visitUpdate(SqlUpdate update) {

        String tableName = "";
        if (update.getTargetTable() instanceof SqlIdentifier) {
            SqlIdentifier id = (SqlIdentifier)update.getTargetTable();
            visitOutputIdentifier(id);
            tableName = id.toString();
        }

        if (update.getTargetColumnList() != null) {
            visitSQLNodeList(update.getTargetColumnList(), tableName);
        }

    }

    /**
     * In a MERGE statement, we record the name of the target table
     * as an output table and the name of the source table as an input table.
     */
    private void visitMerge(SqlMerge merge) {

        if (merge.getTargetTable() instanceof SqlIdentifier) {
            visitOutputIdentifier((SqlIdentifier)merge.getTargetTable());
        }

        if (merge.getSourceTableRef() instanceof SqlIdentifier) {
            visitInputIdentifier((SqlIdentifier)merge.getSourceTableRef());
        } else if (merge.getSourceTableRef() instanceof SqlBasicCall) {
            SqlBasicCall as = (SqlBasicCall)merge.getSourceTableRef();
            if (as.getKind() == AS) {
                visitSourceAs(as);
            }
        }
    }

    private void visitSourceAs(SqlBasicCall basic) {
        SqlNode importantNode = basic.getOperandList().get(0);
        if (importantNode instanceof SqlIdentifier) {
            visitInputIdentifier((SqlIdentifier)importantNode);
        }
    }


    private void visitSqlIdentifier(SqlIdentifier identifier, String tableName) {
        // existing columns for table name
        Set<String> existingElements =  tableColumnMapping.get(tableName);

        if(identifier.names.size() < 2){
            // ignore table names and alias
            if (inputTables.contains(identifier.names.get(0)) || outputTables.contains(identifier.names.get(0)) ||
                    allAlias.contains(identifier.names.get(0))){
                // do nothing
            } else if (CollectionUtils.isNotEmpty(existingElements)){
                existingElements.add(identifier.names.get(0));
                tableColumnMapping.put(tableName, existingElements);
            } else {
                existingElements = new HashSet<>(Collections.singleton(identifier.names.get(0)));
                tableColumnMapping.put(tableName, existingElements);
            }
        } else {
            String alias = identifier.names.get(0);
            String name = identifier.names.get(1);
            String matchingTableName = aliasTableMapping.get(alias);
            if (inputTables.contains(name) || outputTables.contains(name) ||
                    allAlias.contains(name)){
                // do nothing
            } else if (StringUtils.isNotEmpty(matchingTableName)) {
                existingElements =  tableColumnMapping.get(matchingTableName);
                if (CollectionUtils.isNotEmpty(existingElements)) {
                    existingElements.add(name);
                    tableColumnMapping.put(matchingTableName, existingElements);
                } else {
                    existingElements = new HashSet<>(Collections.singleton(name));
                    tableColumnMapping.put(tableName, existingElements);
                }
            } else {
                // do nothing
            }
        }
    }

    private void visitOutputIdentifier(SqlIdentifier id) {
        outputTables.add(id.toString());
    }

    private void visitInputIdentifier(SqlIdentifier id) {
        inputTables.add(id.toString());
    }
}

