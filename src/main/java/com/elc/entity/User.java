package com.elc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2023/09/20/21:50
 * @Description: 登录用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String account;
    private String password;
}
