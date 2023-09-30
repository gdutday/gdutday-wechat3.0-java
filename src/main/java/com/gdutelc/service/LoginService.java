package com.gdutelc.service;

import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.framework.domain.VO.GdutDayWechatUser;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 01:05
 * LoginService
 */
public interface LoginService {


    /**
     * 微信小程序登录
     *
     * @param gdutDayWechatUser
     */
    public AjaxResult gdutDayWechatUserLogin(GdutDayWechatUser gdutDayWechatUser);

}
