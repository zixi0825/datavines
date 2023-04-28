package io.datavines.pipeline.plugin.dolphinscheduler.handler;

import io.datavines.common.entity.QueryColumn;
import io.datavines.pipeline.api.PipelineDataResponse;
import io.datavines.pipeline.api.PipelineDataResponseHandler;

import java.util.Map;

public class PipelineProjectTableHandler implements PipelineDataResponseHandler {


    @Override
    public void handle(PipelineDataResponse response) {
        // 取出ResponseData, 替换Header，然后根据
    }

//    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
//            `name` varchar(255) DEFAULT NULL COMMENT 'project name',
//            `code` bigint(20) NOT NULL COMMENT 'encoding',
//            `description` varchar(255) DEFAULT NULL,
//  `user_id` int(11) DEFAULT NULL COMMENT 'creator id',
//            `flag` tinyint(4) DEFAULT '1' COMMENT '0 not available, 1 available',
//            `create_time` datetime NOT NULL COMMENT 'create time',
//            `update_time` datetime DEFAULT NULL COMMENT 'update time',

//    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
//            `name` varchar(255) DEFAULT NULL COMMENT 'project name',
//            `code` bigint(20) NOT NULL COMMENT 'encoding',
//            `description` varchar(255) DEFAULT NULL,
//    `properties` longtext DEFAULT NULL COMMENT 'project properties map',
//            `creator` int(11) DEFAULT NULL COMMENT 'creator',
//            `create_time` datetime NOT NULL COMMENT 'create time',
//            `update_time` datetime DEFAULT NULL COMMENT 'update time',
    private Map<String, QueryColumn> headerMap() {
        return null;
    }
}
