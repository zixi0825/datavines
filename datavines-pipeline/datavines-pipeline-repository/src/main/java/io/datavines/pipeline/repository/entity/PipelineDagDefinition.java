package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PipelineDagDefinition implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    private Long code;

    private String name;

    private String description;

    private Long projectCode;

    private String releaseState;

    private String properties;

    private String creator;

    private Date createTime;

    private Date updateTime;
}