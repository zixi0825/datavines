package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PipelineDagInstance implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private String name;

    private Long dagDefinitionCode;

    private String dagDefinitionVersion;

    private Long projectCode;

    private String state;

    private String host;

    private Date startTime;

    private Date endTime;

    private Date scheduleTime;

    private Date updateTime;

    private int isSubDag;

    private String properties;
}