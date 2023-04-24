package io.datavines.pipeline.repository.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PipelineTaskRelation implements Serializable {

    private static final long serialVersionUID = -1L;

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

    private String creator;

    private Date createTime;

    private Date updateTime;
}
