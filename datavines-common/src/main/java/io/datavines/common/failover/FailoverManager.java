package io.datavines.common.failover;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FailoverManager {

    private static class Singleton {
        static FailoverManager instance = new FailoverManager();
    }

    public static FailoverManager getInstance() {
        return Singleton.instance;
    }

    private final Set<FailoverListener> registry = new CopyOnWriteArraySet<>();

    public void registry(FailoverListener failoverListener) {
        registry.add(failoverListener);
    }

    public void unRegistry(FailoverListener failoverListener) {
        registry.remove(failoverListener);
    }

    public void handleFailover(String host) {
        if (CollectionUtils.isNotEmpty(registry)) {
            registry.forEach(failoverListener -> failoverListener.handleFailover(host));
        }
    }

    public void handleFailover(List<String> hostList) {
        if (CollectionUtils.isNotEmpty(registry)) {
            registry.forEach(failoverListener -> failoverListener.handleFailover(hostList));
        }
    }
}
