package io.datavines.pipeline.plugin.query;

public class DolphinSchedulerPipelineDagInstance extends AbstractTableQueryBuilder {

    @Override
    public String tableName() {
        return "t_ds_process_instance";
    }
}
