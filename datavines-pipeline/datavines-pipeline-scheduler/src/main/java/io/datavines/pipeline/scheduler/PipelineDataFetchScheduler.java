package io.datavines.pipeline.scheduler;

public class PipelineDataFetchScheduler {

    //-添加pipeline服务配置信息，点击启动同步数据、按照一定节奏进行数据同步
    //--记录每个pipeline服务中每张表的上次同步时间
    //-数据同步逻辑
    //--启动时判断是否存在需要进行同步的Pipeline服务，如果存在则启动同步，同时获取同步时间，如果存在则获取上次同步时间后的数据，否则进行全量同步
    //--同步数据时，根据pipeline服务配置信息，获取需要同步的表，根据上次同步时间获取需要同步的数据，进行同步
    //--同步完成后，更新pipeline服务配置信息中每张表的同步时间
    //--同步完成后，向同步命令表中插入同步命令、pipeline服务ID、期望执行时间
    
}
