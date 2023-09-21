package com.elc.controller;

import com.elc.entity.Result;
import com.elc.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2023/09/20/21:37
 * @Description:
 */
@RestController
@RequestMapping("/gdutDay2/login")
public class LoginController {
    @PostMapping("/login.elc")
    public Result login(User user){
        return null;
    }

}
