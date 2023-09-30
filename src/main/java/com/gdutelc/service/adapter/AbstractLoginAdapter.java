package com.gdutelc.service.adapter;

import com.beust.ah.A;
import com.gdutelc.common.constant.RoleConstant;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.framework.domain.VO.GdutDayWechatUser;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.LoginService;

import javax.validation.constraints.NotNull;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 01:03
 * AbstractLoginAdapter
 */
public abstract class AbstractLoginAdapter implements LoginService {


    @Override
    public AjaxResult gdutDayWechatUserLogin(@NotNull GdutDayWechatUser gdutDayWechatUser) {
        switch (gdutDayWechatUser.getLoginType()) {
            case 1 -> {
                // 只有本科才能使用教务系统直接登录
                if (!RoleConstant.UNDER_GRADUATE.equals(gdutDayWechatUser.getUserType())) {
                    throw new ServiceException("登录类型错误！");
                }
                return jxfwLogin(gdutDayWechatUser);
            }
            case 2 -> {
                return ehallLogin(gdutDayWechatUser);
            }
        }
         return AjaxResult.error();
    }


    /**
     * 本科教务管理
     *
     * @param gdutDayWechatUser 小程序登录VO
     */
    public abstract AjaxResult jxfwLogin(GdutDayWechatUser gdutDayWechatUser);

    /**
     * 统一登录
     *
     * @param gdutDayWechatUser 小程序登录VO
     */
    public abstract AjaxResult ehallLogin(GdutDayWechatUser gdutDayWechatUser);


}
