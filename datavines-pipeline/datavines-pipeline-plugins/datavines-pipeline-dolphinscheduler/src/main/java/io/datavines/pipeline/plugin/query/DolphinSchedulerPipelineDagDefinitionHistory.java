package io.datavines.pipeline.plugin.query;

public class DolphinSchedulerPipelineDagDefinitionHistory extends AbstractTableQueryBuilder {

    @Override
    public String tableName() {
        return "t_ds_process_definition_log";
    }
}
