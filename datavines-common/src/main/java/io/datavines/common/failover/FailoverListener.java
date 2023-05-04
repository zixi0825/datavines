package io.datavines.common.failover;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public interface FailoverListener {

    public void handleFailover(String host);

    public void handleFailover(List<String> hostList);
}
