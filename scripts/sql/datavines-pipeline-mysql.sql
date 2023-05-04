-- ----------------------------
-- Table structure for dv_pl_pipeline_source
-- ----------------------------
DROP TABLE IF EXISTS `dv_pl_pipeline_source`;
CREATE TABLE `dv_pl_pipeline_source` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `uuid` varchar(64) NOT NULL COMMENT '数据管道服务UUID',
    `name` varchar(255) NOT NULL COMMENT '数据管道服务名称',
    `type` varchar(255) NOT NULL COMMENT '数据管道服务类型',
    `connection_type` varchar(255) NOT NULL COMMENT '数据管道服务类连接类型',
    `param` text NOT NULL COMMENT '数据管道服务参数',
    `param_code` varchar(255) NULL COMMENT '数据管道服务参数MD5值',
    `workspace_id` bigint(20) NOT NULL COMMENT '工作空间ID',
    `create_by` bigint(20) NOT NULL COMMENT '创建用户ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` bigint(20) NOT NULL COMMENT '更新用户ID',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `pipeline_source_un` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据管道服务';

-- ----------------------------
-- Table structure for dv_pl_command
-- ----------------------------
DROP TABLE IF EXISTS `dv_pl_command`;
CREATE TABLE `dv_pl_command` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `parameter` text COMMENT 'json command parameters',
    `task_id` bigint(20) NOT NULL COMMENT 'task id',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='获取数据命令';

-- ----------------------------
-- Table structure for dv_pl_data_fetch_task
-- ----------------------------
DROP TABLE IF EXISTS `dv_pl_data_fetch_task`;
CREATE TABLE `dv_pl_data_fetch_task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `pipeline_source_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '数据管道服务ID',
    `status` int(11) DEFAULT NULL COMMENT '任务状态',
    `parameter` text COMMENT '任务参数',
    `execute_host` varchar(255) DEFAULT NULL COMMENT '执行任务的主机',
    `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据管道数据抓取任务';

-- ----------------------------
-- Table structure for dv_datasource
-- ----------------------------
DROP TABLE IF EXISTS `dv_pl_sync_data_offset`;
CREATE TABLE `dv_pl_data_offset` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `source_uuid` varchar(64) NOT NULL COMMENT '数据管道服务UUID',
    `database_name` varchar(255) NOT NULL COMMENT '数据库名称',
    `table_name` varchar(255) NOT NULL COMMENT '表名称',
    `last_seen_time` varchar(255) NOT NULL COMMENT '上次读取数据的时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据管道数据同步时间';

DROP TABLE IF EXISTS `dv_pl_project`;
CREATE TABLE `dv_pl_project` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name` varchar(255) DEFAULT NULL COMMENT 'project name',
    `code` bigint(20) NOT NULL COMMENT 'encoding',
    `description` varchar(255) DEFAULT NULL,
    `properties` longtext DEFAULT NULL COMMENT 'project properties map',
    `creator` varchar(255) DEFAULT NULL COMMENT 'creator',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime DEFAULT NULL COMMENT 'update time',
    PRIMARY KEY (`id`),
    KEY `creator_index` (`creator`) USING BTREE,
    UNIQUE KEY `unique_name`(`name`),
    UNIQUE KEY `unique_code`(`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_dag_definition`;
CREATE TABLE `dv_pl_dag_definition` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `code` bigint(20) NOT NULL COMMENT 'unique encoding',
    `name` varchar(255) DEFAULT NULL COMMENT 'dag definition name',
    `version` int(11) DEFAULT '0' COMMENT 'dag definition version',
    `description` varchar(255) COMMENT 'dag description',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `release_state` tinyint(4) DEFAULT NULL COMMENT 'dag definition release state：0:offline,1:online',
    `properties` longtext DEFAULT NULL COMMENT 'dag definition properties map',
    `creator` varchar(255) DEFAULT NULL COMMENT 'dag definition creator',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`,`code`),
    UNIQUE KEY `dag_unique` (`name`,`project_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_dag_definition_history`;
CREATE TABLE `dv_pl_dag_definition_history` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `code` bigint(20) NOT NULL COMMENT 'unique encoding',
    `name` varchar(255) DEFAULT NULL COMMENT 'dag definition name',
    `version` int(11) DEFAULT '0' COMMENT 'dag definition version',
    `description` varchar(255) COMMENT 'dag description',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `release_state` tinyint(4) DEFAULT NULL COMMENT 'dag definition release state：0:offline,1:online',
    `properties` longtext DEFAULT NULL COMMENT 'dag properties map',
    `operator` varchar(255) DEFAULT NULL COMMENT 'operator user name',
    `operate_time` datetime DEFAULT NULL COMMENT 'operate time',
    `creator` varchar(255) DEFAULT NULL COMMENT 'dag definition creator',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`,`code`),
    UNIQUE KEY `dag_unique` (`name`,`project_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_task_definition`;
CREATE TABLE `dv_pl_task_definition` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `code` bigint(20) NOT NULL COMMENT 'encoding',
    `name` varchar(255) DEFAULT NULL COMMENT 'task definition name',
    `version` int(11) DEFAULT '0' COMMENT 'task definition version',
    `description` varchar(255) COMMENT 'description',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `task_type` varchar(50) NOT NULL COMMENT 'task type',
    `task_execute_type` int(11) DEFAULT '0' COMMENT 'task execute type: 0-batch, 1-stream',
    `task_params` longtext COMMENT 'task custom parameters',
    `task_priority` tinyint(4) DEFAULT '2' COMMENT 'task priority',
    `properties` longtext DEFAULT NULL COMMENT 'task definition properties map',
    `creator` varchar(255) DEFAULT NULL COMMENT 'task definition creator',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_task_definition_history`;
CREATE TABLE `dv_pl_task_definition_history` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `code` bigint(20) NOT NULL COMMENT 'encoding',
    `name` varchar(255) DEFAULT NULL COMMENT 'task definition name',
    `version` int(11) DEFAULT '0' COMMENT 'task definition version',
    `description` varchar(255) COMMENT 'description',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `task_type` varchar(50) NOT NULL COMMENT 'task type',
    `task_execute_type` int(11) DEFAULT '0' COMMENT 'task execute type: 0-batch, 1-stream',
    `task_params` longtext COMMENT 'task custom parameters',
    `task_priority` tinyint(4) DEFAULT '2' COMMENT 'task priority',
    `properties` longtext DEFAULT NULL COMMENT 'task definition properties map',
    `operator` varchar(255) DEFAULT NULL COMMENT 'operator user name',
    `operate_time` datetime DEFAULT NULL COMMENT 'operate time',
    `creator` varchar(255) DEFAULT NULL COMMENT 'task definition creator',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`,`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_dag_task_relation`;
CREATE TABLE `dv_pl_dag_task_relation` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `dag_definition_code` bigint(20) NOT NULL COMMENT 'dag code',
    `dag_definition_version` int(11) NOT NULL COMMENT 'dag version',
    `pre_task_code` bigint(20) NOT NULL COMMENT 'pre task code',
    `pre_task_version` int(11) NOT NULL COMMENT 'pre task version',
    `post_task_code` bigint(20) NOT NULL COMMENT 'post task code',
    `post_task_version` int(11) NOT NULL COMMENT 'post task version',
    `condition_type` tinyint(2) DEFAULT NULL COMMENT 'condition type : 0 none, 1 judge 2 delay',
    `condition_params` text COMMENT 'condition params(json)',
    `properties` longtext DEFAULT NULL COMMENT 'task relation properties map',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`),
    KEY `idx_code` (`project_code`,`dag_definition_code`),
    KEY `idx_pre_task_code_version` (`pre_task_code`,`pre_task_version`),
    KEY `idx_post_task_code_version` (`post_task_code`,`post_task_version`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for dv_pl_dag_task_relation_log
-- ----------------------------
DROP TABLE IF EXISTS `dv_pl_dag_task_relation_history`;
CREATE TABLE `dv_pl_dag_task_relation_history` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'self-increasing id',
    `name` varchar(255) DEFAULT NULL COMMENT 'relation name',
    `project_code` bigint(20) NOT NULL COMMENT 'project code',
    `dag_definition_code` bigint(20) NOT NULL COMMENT 'dag code',
    `dag_definition_version` int(11) NOT NULL COMMENT 'dag version',
    `pre_task_code` bigint(20) NOT NULL COMMENT 'pre task code',
    `pre_task_version` int(11) NOT NULL COMMENT 'pre task version',
    `post_task_code` bigint(20) NOT NULL COMMENT 'post task code',
    `post_task_version` int(11) NOT NULL COMMENT 'post task version',
    `condition_type` tinyint(2) DEFAULT NULL COMMENT 'condition type : 0 none, 1 judge 2 delay',
    `condition_params` text COMMENT 'condition params(json)',
    `properties` longtext DEFAULT NULL COMMENT 'task relation properties map',
    `operator` varchar(255) DEFAULT NULL COMMENT 'operator user',
    `operate_time` datetime DEFAULT NULL COMMENT 'operate time',
    `create_time` datetime NOT NULL COMMENT 'create time',
    `update_time` datetime NOT NULL COMMENT 'update time',
    PRIMARY KEY (`id`),
    KEY `idx_dag_code_version` (`dag_definition_code`,`dag_definition_version`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE = utf8_bin;

DROP TABLE IF EXISTS `dv_pl_dag_instance`;
CREATE TABLE `dv_pl_dag_instance` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name` varchar(255) DEFAULT NULL COMMENT 'dag instance name',
    `project_code` bigint(20) DEFAULT NULL COMMENT 'project code',
    `dag_definition_code` bigint(20) NOT NULL COMMENT 'dag definition code',
    `dag_definition_version` int(11) DEFAULT '0' COMMENT 'dag definition version',
    `state` tinyint(4) DEFAULT NULL COMMENT 'dag instance Status: 0 commit succeeded, 1 running, 2 prepare to pause, 3 pause, 4 prepare to stop, 5 stop, 6 fail, 7 succeed, 8 need fault tolerance, 9 kill, 10 wait for thread, 11 wait for dependency to complete',
    `host` varchar(135) DEFAULT NULL COMMENT 'dag instance host',
    `start_time` datetime DEFAULT NULL COMMENT 'dag instance start time',
    `end_time` datetime DEFAULT NULL COMMENT 'dag instance end time',
    `schedule_time` datetime DEFAULT NULL COMMENT 'schedule time',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_sub_dag` int(11) DEFAULT '0' COMMENT 'flag, whether the dag is sub dag',
    `properties` longtext DEFAULT NULL COMMENT 'dag instance run properties map',
    PRIMARY KEY (`id`),
    KEY `dag_instance_index` (`dag_definition_code`,`id`) USING BTREE,
    KEY `start_time_index` (`start_time`,`end_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `dv_pl_task_instance`;
CREATE TABLE `dv_pl_task_instance` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'key',
    `name` varchar(255) DEFAULT NULL COMMENT 'task name',
    `task_type` varchar(50) NOT NULL COMMENT 'task type',
    `task_execute_type` int(11) DEFAULT '0' COMMENT 'task execute type: 0-batch, 1-stream',
    `task_definition_code` bigint(20) NOT NULL COMMENT 'task definition code',
    `task_definition_version` int(11) DEFAULT '0' COMMENT 'task definition version',
    `dag_instance_id` int(11) DEFAULT NULL COMMENT 'dag instance id',
    `dag_instance_name` varchar(255) DEFAULT NULL COMMENT 'dag instance name',
    `project_code` bigint(20) DEFAULT NULL COMMENT 'project code',
    `state` tinyint(4) DEFAULT NULL COMMENT 'Status: 0 commit succeeded, 1 running, 2 prepare to pause, 3 pause, 4 prepare to stop, 5 stop, 6 fail, 7 succeed, 8 need fault tolerance, 9 kill, 10 wait for thread, 11 wait for dependency to complete',
    `submit_time` datetime DEFAULT NULL COMMENT 'task submit time',
    `start_time` datetime DEFAULT NULL COMMENT 'task start time',
    `end_time` datetime DEFAULT NULL COMMENT 'task end time',
    `host` varchar(135) DEFAULT NULL COMMENT 'host of task running on',
    `pid` int(4) DEFAULT NULL COMMENT 'pid of task',
    `app_ids` text COMMENT 'yarn app id',
    `task_params` longtext COMMENT 'task custom parameters',
    `properties` longtext DEFAULT NULL COMMENT 'taak instance run properties map',
    PRIMARY KEY (`id`),
    KEY `dag_instance_id` (`dag_instance_id`) USING BTREE,
    KEY `idx_code_version` (`task_definition_code`, `task_definition_version`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;