package com.gdutelc.domain.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private String cookies;

    private Integer userType;

    public BaseRequestDto(String cookies, Integer userType) {
        this.cookies = cookies;
        this.userType = userType;
    }
}
