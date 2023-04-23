package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PipelineTaskInstance {

    private Long id;

    private String name;

    private Long projectCode;

    private String taskType;

    private Integer taskExecuteType;

    private Long taskDefinitionCode;

    private String taskDefinitionVersion;

    private Long dagInstanceId;

    private String dagInstanceName;

    private int state;

    private Date submitTime;

    private Date startTime;

    private Date endTime;

    private String host;

    private String pid;

    private String appIds;

    private String taskParams;

    private String properties;

}