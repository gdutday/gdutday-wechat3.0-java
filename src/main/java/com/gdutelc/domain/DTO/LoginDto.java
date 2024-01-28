package com.gdutelc.domain.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;


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

    private String campus;


    private String weCookies;


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
