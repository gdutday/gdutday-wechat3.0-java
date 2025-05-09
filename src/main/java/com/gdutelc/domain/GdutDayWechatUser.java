package com.gdutelc.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/28 23:09
 * User
 */
@Setter
@Getter
@ToString
public class GdutDayWechatUser implements Serializable {

    //(value = "用户名")
    @NotBlank(message = "账号不能为空")
    private String user;

    //(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    //(value = "验证码")
    private String code;

    //(value = "用户身份类型，1-学生，2-研究生，3-老师")
    @Range(min=1,max=3,message = "用户类型错误！")
    private Integer userType;

    //(value = "用户登录类型，1-jxfw登录(仅限制本科生)，2-hell登录")
    @Range(min=1,max=2,message = "登录类型异常！")
    private Integer loginType;

    //(value = "jSessionid, 兼容V2")
    private String jSessionId;


    public GdutDayWechatUser(String user, String password, String code, Integer userType, Integer loginType, String jSessionId) {
        this.user = user;
        this.password = password;
        this.code = code;
        this.userType = userType;
        this.loginType = loginType;
        this.jSessionId = jSessionId;
    }
}
