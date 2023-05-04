package io.datavines.pipeline.plugin.query;

import io.datavines.pipeline.api.TableQueryBuilder;

public abstract class AbstractTableQueryBuilder implements TableQueryBuilder {

    @Override
    public String buildQuery(String lastSeenTime) {
        return "select * from " + tableName() + " where update_time >= " + lastSeenTime;
    }
}
