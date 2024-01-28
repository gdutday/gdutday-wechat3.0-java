package com.gdutelc.domain.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/28 20:46
 * BaseRequetsDto 基础请求类
 */
@Getter
@Setter
@ToString
public class BaseRequestDto implements Serializable {

    @NotBlank(message = "cookie不能为空")
    private String cookies;

    @NotNull(message = "用户类型不能为空")
    @Range(max = 3, min = 0, message = "请输入正确的userType")
    private Integer userType;

    public BaseRequestDto(String cookies, Integer userType) {
        this.cookies = cookies;
        this.userType = userType;
    }
}
