package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.util.Date;

@Data
public class PipelineTaskRelationHistory {

    private Long id;

    private Long projectCode;

    private Long dagDefinitionCode;

    private String dagDefinitionVersion;

    private Long preTaskCode;

    private String preTaskVersion;

    private Long postTaskCode;

    private String postTaskVersion;

    private int conditionType;

    private String conditionParams;

    private String properties;

    private String operator;

    private Date operateTime;

    private String creator;

    private Date createTime;

    private Date updateTime;
}
