package com.gdutelc.domain.DTO;

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
   // "验证码")
    private String verCode;
    //"cookie")
    private String jSessionId;
}
