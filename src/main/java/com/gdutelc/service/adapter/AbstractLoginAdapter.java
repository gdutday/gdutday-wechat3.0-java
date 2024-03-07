package com.gdutelc.service.adapter;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.LoginService;
import com.gdutelc.utils.*;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;

import static com.gdutelc.common.constant.LoginConstant.EHALL_LOGIN;
import static com.gdutelc.common.constant.LoginConstant.JXFW_LOGIN;
import static com.gdutelc.common.constant.RoleConstant.*;
import static com.gdutelc.common.constant.UrlConstant.*;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 01:03
 * AbstractLoginAdapter
 */
@Slf4j
public abstract class AbstractLoginAdapter implements LoginService {

    @Resource
    private OkHttpUtils okHttpUtils;

    @Override
    public LoginDto gdutDayWechatUserLogin(@NotNull GdutDayWechatUser gdutDayWechatUser) {
        switch (gdutDayWechatUser.getLoginType()) {
            case JXFW_LOGIN -> {
                // 只有本科才能使用教务系统直接登录
                if (!UNDER_GRADUATE.equals(gdutDayWechatUser.getUserType())) {
                    throw new ServiceException("登录类型错误！",HttpStatus.f003);
                }
                return jxfwLogin(gdutDayWechatUser);
            }
            case EHALL_LOGIN -> {
                return this.ehallLogin(gdutDayWechatUser);
            }
        }
        return null;
    }

    public String loginDecrypt(@NotBlank String loginInfo) {
        String loginInfoStr;
        try {
            loginInfoStr = LiUtils.decrypt(loginInfo);
        } catch (Exception e) {
            throw new ServiceException("Internal server error!", HttpStatus.ERROR);
        }
        return loginInfoStr;
    }

    /**
     * 本科教务管理
     *
     * @param gdutDayWechatUser 小程序登录VO
     */
    public abstract LoginDto jxfwLogin(GdutDayWechatUser gdutDayWechatUser);

    /**
     * check block
     *
     * @param user user
     */
    public boolean checkBlock(String user) {
        if (StringUtils.isEmpty(user)) {
            throw new ServiceException("请检查请求参数！", HttpStatus.f002);
        }
        OkHttpClient myokHttpClient = okHttpUtils.makeOkhttpClient();
        HashMap<String, String> map = new HashMap<>(3);
        map.put("username", user);
        map.put("_", Long.toString(System.currentTimeMillis()));
        RequestBody requestBody1 = JsoupUtils.map2PostUrlCodeString(map);
        //检查是否需要滑块认证
        try (Response response1 = okHttpUtils.postByFormUrl(myokHttpClient, CHECK_BLOCK_URL, requestBody1)) {
            JSONObject object;
            if (response1.code() != 200 || response1.body() == null) {
                log.error("统一认证滑块链接请求异常");
                throw new ServiceException("网络请求异常，请重试！",HttpStatus.f5001);
            }
            object = JSONObject.parseObject(response1.body().string());
            boolean isNeed = (boolean) object.get("isNeed");
            if (isNeed) {
                // 需要手动过滑块
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            throw new ServiceException("网络请求异常，请重试！",HttpStatus.f5001);
        }
    }

    /**
     * 统一登录
     * 全局共用一个okhttp，但是每次都复用okhttpclinent,
     * 严格使用工具new okhttp,登录的时候直接传入new的cookie,最后记得释放和清空
     * 除登录外其他可直接复用全局的，需要先清除
     * 1.拿到页面参数
     * 2.通过登录
     * 3.身份校验
     * 4.通过认证
     *
     * @param gdutDayWechatUser 小程序登录VO
     */
    public LoginDto ehallLogin(GdutDayWechatUser gdutDayWechatUser) {
        GdutDayCookieJar cookieJar = new GdutDayCookieJar();
        OkHttpClient myokHttpClient = okHttpUtils.makeOkhttpClient(cookieJar);
        // 取消先验滑块，后续单独提供接口或者前端直接请求
        // this.checkBlock(gdutDayWechatUser, myokHttpClient);
        String cookieStr = null;
        try {
            this.preLogin(gdutDayWechatUser, myokHttpClient);
            // 获得用户信息
            // 从登录的账号获得用户类型
            int userType = getUserInfoFromEhall(myokHttpClient);
            if (userType < 0) {
                throw new ServiceException("请检查请求参数！", HttpStatus.f002);
            }
            if (userType == UNDER_GRADUATE) {
                // 本科登录，传入本科教务系统地址
                this.postLoginByUrl(UNDER_GRADUATE_LOGIN, myokHttpClient);
                cookieStr = OkHttpUtils.getCookies(cookieJar.cookies);
            } else if (userType == GRADUATE) {
                // 研究生登录，传入研究生登录地址
                this.postLoginByUrl(UrlConstant.GRADUATE_EHALL_LOGIN, myokHttpClient);
                cookieStr = OkHttpUtils.getCookieRemoveShortWEU(cookieJar.cookies);
                return new LoginDto(cookieStr, userType);
            } else if (userType == TEACHER) {
                this.postLoginByUrl(UrlConstant.TEACHER__EHALL_LOGIN, myokHttpClient);
                cookieStr = OkHttpUtils.getCookies(cookieJar.cookies);
            }
            return new LoginDto(cookieStr, userType);
        } catch (IOException e) {
            throw new ServiceException("网络请求异常，请重试！", HttpStatus.f5001);
        }
    }


    /**
     * Pre login to ehall
     *
     * @param gdutDayWechatUser userInfo
     */
    public abstract void preLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient);


    /**
     * 从ehhall的接口获得用户类型，兼容三端
     *
     * @param myokHttpClient okhtpClient
     * @return 用户类型
     */
    public Integer getUserInfoFromEhall(OkHttpClient myokHttpClient) throws IOException {
        String html;
        String userNum;
        // 从接口获得用户类型
        try (Response response = okHttpUtils.get(myokHttpClient, EHALL_USER_INFO)) {
            assert response.body() != null;
            html = response.body().string();
            response.body().close();
        }
        userNum = OkHttpUtils.getUid(html);
        if (userNum == null) {
            throw new ServiceException("账号或密码错误！", HttpStatus.f005);
        }
        // 获得用户第一位编号
        int userType = Integer.parseInt(userNum.substring(0, 1));
        switch (userType) {
            case 3 -> {
                // 本科生
                return UNDER_GRADUATE;
            }
            case 2 -> {
                // 研究生
                return GRADUATE;
            }
            case 0 -> {
                // 老师
                return TEACHER;
            }
        }
        return -1;

    }

    /**
     * Login in to another system, just send a post request to OSS
     *
     * @param url          url
     * @param okHttpClient okHttpClient
     */
    public abstract void postLoginByUrl(String url, OkHttpClient okHttpClient);

}
