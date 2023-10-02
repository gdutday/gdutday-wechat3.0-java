package com.gdutelc.domain.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/10/2 11:14
 * UserInfoVo
 */
@Getter
@Setter
public class BasicVO implements Serializable {

    @ApiModelProperty("用户Cookies")
    @NotEmpty(message = "请检查输入参数")
    private String weCookies;

    public BasicVO(String weCookies) {
        this.weCookies = weCookies;
    }
}
