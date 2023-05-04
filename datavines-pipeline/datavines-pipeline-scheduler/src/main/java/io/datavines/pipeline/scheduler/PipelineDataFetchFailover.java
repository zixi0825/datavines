package io.datavines.pipeline.scheduler;

import io.datavines.common.failover.FailoverListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PipelineDataFetchFailover implements FailoverListener {

    @Override
    public void handleFailover(String host) {
        //先判断当前是否持有
        //从syncDataOffset中获取所有表上次同步的时间，如果有表不存在上次同步时间，则上次同步时间设置为一个星期之前
        //构造PipelineFetchDataRequest放入RequestQueue中
        //启动PipelineDataFetch
    }

    @Override
    public void handleFailover(List<String> hostList) {

    }
}
