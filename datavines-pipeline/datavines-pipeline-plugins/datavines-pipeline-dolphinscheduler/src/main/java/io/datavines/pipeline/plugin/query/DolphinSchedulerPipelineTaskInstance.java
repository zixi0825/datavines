package io.datavines.pipeline.plugin.query;

public class DolphinSchedulerPipelineTaskInstance extends AbstractTableQueryBuilder {

    @Override
    public String buildQuery(String lastSeenTime) {
        return "select * from " + tableName() + " where start_time >= " + lastSeenTime + " or end_time >= " + lastSeenTime;
    }

    @Override
    public String tableName() {
        return "t_ds_task_instance";
    }
}
