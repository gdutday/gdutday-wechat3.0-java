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


    /**
     * 课程名称
     */
    @JSONField(name = "KCMC")
    private String courseName;

    /**
     * 子课程类型，比如科学类还是其他
     */
    private String courseType;

    /**
     * 学分
     */
    @JSONField(name = "XF")
    private String credit;

    /**
     * 绩点
     */
    private String gpa;

    /**
     * 成绩
     */
    @JSONField(name = "DYBFZCJ")
    private String result;
    /**
     * 学期
     */
    @JSONField(name = "XNXQDM")
    private String term;
    /**
     * 课程类型
     */
    @JSONField(name = "KCLBMC")
    private String type;

    /**
     * 选修还是必修
     */
    private String option;
}
