package com.gdutelc.domain.DTO;

import com.alibaba.fastjson2.annotation.JSONField;
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


    @JSONField(name = "cn")
    private String courseName;

    @JSONField(name = "cType")
    private String courseType;

    @JSONField(name = "credit")
    private Double credit;

    @JSONField(name = "gp")
    private Double gap;

    @JSONField(name = "result")
    private Double result;

    private String term;

    private String type;
}
