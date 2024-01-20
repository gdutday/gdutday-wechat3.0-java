package com.gdutelc.domain.DTO;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:37
 * ScheduleInfoDto
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInfoDto implements Serializable {

    // "课程名称"
    @JSONField(name = "cn")
    private String courseName;

    //"课程地点")
    @JSONField(name = "ad")
    private String coursePlace;

    //"课程教师")
    @JSONField(name = "tn")
    private String courseTeacher;

    //"周次")
    @JSONField(name = "w")
    private String courseWeek;

    //"星期")
    @JSONField(name = "wd")
    private String courseDay;

    //"节次")
    @JSONField(name = "cs")
    private String courseSection;

    //"课程描述")
    @JSONField(name = "cc")
    private String courseDescription;


}
