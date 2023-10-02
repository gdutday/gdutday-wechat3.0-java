package com.gdutelc.domain.DTO;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:53
 * ExamScoreDTO
 */
@Getter
@Setter
@ToString
public class ExamScoreDto implements Serializable {

    @ApiModelProperty(value = "课程名称")
    @JSONField(name = "cn")
    private String courseName;

    @ApiModelProperty(value = "未知...从cType得到,猜测是courseType")
    @JSONField(name = "cType")
    private String courseType;

    @ApiModelProperty(value = "学分")
    @JSONField(name = "credit")
    private Double credit;

    @ApiModelProperty(value = "绩点，计算出来的，研究生和本科计算方式不同，具体见代码")
    @JSONField(name = "gp")
    private Double gap;

    @ApiModelProperty(value = "成绩")
    @JSONField(name = "result")
    private Double result;
    @ApiModelProperty(value = "学期")
    private String term;

    private String type;
}
