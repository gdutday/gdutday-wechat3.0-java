package com.gdutelc.domain.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 10:47
 * LoginDto
 */
@Getter
@Setter
@ToString
public class LoginDto {

    @ApiModelProperty(value = "校区，调整后固定为大学城作息，兼容V2且防止以后又更改")
    private String campus;


    @ApiModelProperty(value = "后续使用的Cookies,同jSessionId值")
    private String weCookies;


    @ApiModelProperty(value = "用户身份类型，1-学生，2-研究生，3-老师")
    @Max(value = 3, message = "登录类型错误")
    @Min(value = 1, message = "登录类型错误")
    private Integer userType;


    public LoginDto(String campus, String weCookies, Integer userType) {
        this.campus = campus;
        this.weCookies = weCookies;
        this.userType = userType;
    }
    public LoginDto(String weCookies, Integer userType) {
        this.campus = "大学城校区";
        this.weCookies = weCookies;
        this.userType = userType;
    }
}
