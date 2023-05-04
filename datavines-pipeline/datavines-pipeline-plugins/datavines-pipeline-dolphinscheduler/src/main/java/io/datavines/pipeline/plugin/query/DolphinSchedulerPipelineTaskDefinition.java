package io.datavines.pipeline.plugin.query;

public class DolphinSchedulerPipelineTaskDefinition extends AbstractTableQueryBuilder {

    @Override
    public String tableName() {
        return "t_ds_task_definition";
    }
}
