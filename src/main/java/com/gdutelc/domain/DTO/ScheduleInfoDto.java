package com.gdutelc.domain.DTO;
import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("节次")
    @JSONField(name = "cs")
    private String courseSection;

    @ApiModelProperty("课程描述")
    @JSONField(name = "cc")
    private String courseDescription;


}
