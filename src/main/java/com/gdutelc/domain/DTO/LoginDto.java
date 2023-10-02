package com.gdutelc.domain.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

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


    public LoginDto(String campus, String weCookies) {
        this.campus = campus;
        this.weCookies = weCookies;
    }

}
