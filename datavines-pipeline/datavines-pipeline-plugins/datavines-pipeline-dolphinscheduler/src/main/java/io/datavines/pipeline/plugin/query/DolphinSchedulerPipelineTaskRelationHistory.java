package io.datavines.pipeline.plugin.query;

public class DolphinSchedulerPipelineTaskRelationHistory extends AbstractTableQueryBuilder {

    @Override
    public String tableName() {
        return "t_ds_task_relation_log";
    }
}
