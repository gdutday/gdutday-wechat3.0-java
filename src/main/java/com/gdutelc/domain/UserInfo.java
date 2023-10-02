package com.gdutelc.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import com.beust.ah.A;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:21
 * UserInfo
 */

@Getter
@Setter
@ToString
public class UserInfo implements Serializable {


    @ApiModelProperty("用户名称")
    @JSONField(name = "XH")
    private Long userName;

    @JSONField(name = "XM")
    @ApiModelProperty("用户学号")
    private String userNum;

    @ApiModelProperty("学院ID")
    @JSONField(name = "YXDM")
    private String departmentId;

    @ApiModelProperty("学院名称")
    @JSONField(name = "YXDM_DISPLAY")
    private String departmentName;


    @JSONField(name = "YXYWMC")
    @ApiModelProperty("学院英文名称")
    private String departmenEnName;

    public UserInfo(Long userName, String userNum, String departmentId, String departmentName, String departmenEnName) {
        this.userName = userName;
        this.userNum = userNum;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmenEnName = departmenEnName;
    }
}
