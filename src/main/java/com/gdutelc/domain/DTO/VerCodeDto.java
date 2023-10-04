package com.gdutelc.domain.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2023/10/04/15:42
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerCodeDto {
    @ApiModelProperty("验证码")
    private String verCode;
    @ApiModelProperty("cookie")
    private String jSessionId;
}
