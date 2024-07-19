RENAME TABLE dv_catalog_metadata_fetch_command TO dv_common_task_command;
RENAME TABLE dv_catalog_metadata_fetch_task TO dv_common_task;
RENAME TABLE dv_catalog_metadata_fetch_task_schedule TO dv_common_task_schedule;

ALTER TABLE dv_actual_values MODIFY COLUMN actual_value decimal(20,4) NULL COMMENT '实际值';
ALTER TABLE dv_common_task ADD task_type varchar(128) NULL COMMENT '任务类型';
ALTER TABLE dv_common_task_schedule ADD task_type varchar(128) NULL COMMENT '任务类型';

ALTER TABLE dv_job_execution_result MODIFY COLUMN actual_value decimal(20,4) DEFAULT NULL COMMENT '实际值';
ALTER TABLE dv_job_execution_result MODIFY COLUMN expected_value decimal(20,4) DEFAULT NULL COMMENT '期望值';
ALTER TABLE dv_job_execution_result ADD score decimal(20,4) DEFAULT 0 COMMENT '质量评分';

-- ----------------------------
-- Table structure for dv_job_quality_report
-- ----------------------------
DROP TABLE IF EXISTS `dv_job_quality_report`;
CREATE TABLE `dv_job_quality_report` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `datasource_id` bigint(20) DEFAULT NULL COMMENT '数据源ID',
    `entity_level` varchar(128) DEFAULT NULL COMMENT '实体级别：DATASOURCE，DATABASE，TABLE，COLUMN',
    `database_name` varchar(128) DEFAULT NULL COMMENT '数据库名称',
    `table_name` varchar(128) DEFAULT NULL COMMENT '表名称',
    `column_name` varchar(128) DEFAULT NULL COMMENT '列名称',
    `score` decimal(20,4) DEFAULT NULL COMMENT '质量评分',
    `report_date` date DEFAULT NULL COMMENT '报告日期',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量报告';

-- ----------------------------
-- Table structure for dv_job_execution_result_report_rel
-- ----------------------------
DROP TABLE IF EXISTS `dv_job_execution_result_report_rel`;
CREATE TABLE `dv_job_execution_result_report_rel` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `quality_report_id` bigint(20) NOT NULL COMMENT '质量报告ID',
    `job_execution_result_id` bigint(20) NOT NULL COMMENT '作业执行结果ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `dv_execution_report_rel_un` (`job_execution_result_id`,`quality_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质量报告和执行结果关联关系';


-- update  collate
alter table dv_catalog_entity_instance modify fully_qualified_name varchar(255) collate utf8mb4_bin not null comment '全限定名';


update dv_common_task set task_type = 'catalog_metadata_fetch' where task_type is null or task_type = '';
update dv_common_task_schedule set task_type = 'catalog_metadata_fetch' where task_type is null or task_type = '';
