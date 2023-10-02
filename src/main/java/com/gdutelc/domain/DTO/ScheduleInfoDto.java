package com.gdutelc.domain.DTO;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class ScheduleInfoDto implements Serializable {

    @ApiModelProperty("课程名称")
    @JSONField(name = "cn")
    private String courseName;

    @ApiModelProperty("课程地点")
    @JSONField(name = "ad")
    private String coursePlace;

    @ApiModelProperty("课程教师")
    @JSONField(name = "tn")
    private String courseTeacher;

    @ApiModelProperty("周次")
    @JSONField(name = "w")
    private String courseWeek;

    @ApiModelProperty("星期")
    @JSONField(name = "wd")
    private String courseDay;

    @ApiModelProperty("课程描述")
    @JSONField(name = "cc")
    private String courseDescription;

    @ApiModelProperty("前端显示的颜色，保证连续的两个颜色不一致即可，id号随便")
    @JSONField(name = "id")
    private String color;

    @ApiModelProperty("结束周次情况")
    @JSONField(name = "xs")
    private String courseEndWeek;

    public ScheduleInfoDto(String courseName, String coursePlace, String courseTeacher, String courseWeek, String courseDay, String courseDescription, String color, String courseEndWeek) {
        this.courseName = courseName;
        this.coursePlace = coursePlace;
        this.courseTeacher = courseTeacher;
        this.courseWeek = courseWeek;
        this.courseDay = courseDay;
        this.courseDescription = courseDescription;
        this.color = color;
        this.courseEndWeek = courseEndWeek;
    }
}
