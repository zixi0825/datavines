package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PipelineProject {

    private Long id;

    private String name;

    private Long code;

    private String description;

    private String creator;

    private Date createTime;

    private Date updateTime;

}
