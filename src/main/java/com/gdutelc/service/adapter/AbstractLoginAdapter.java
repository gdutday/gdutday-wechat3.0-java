package com.gdutelc.service.adapter;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.LoginService;
import com.gdutelc.utils.GdutDayCookieJar;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.OkHttpUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;

import static com.gdutelc.common.constant.LoginConstant.EHALL_LOGIN;
import static com.gdutelc.common.constant.LoginConstant.JXFW_LOGIN;
import static com.gdutelc.common.constant.RoleConstant.*;
import static com.gdutelc.common.constant.UrlConstant.CHECK_BLOCK_URL;
import static com.gdutelc.common.constant.UrlConstant.GRADUATE_USER_INFO;

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
        //@TODO:密码解密
        switch (gdutDayWechatUser.getLoginType()) {
            case JXFW_LOGIN -> {
                // 只有本科才能使用教务系统直接登录
                if (!UNDER_GRADUATE.equals(gdutDayWechatUser.getUserType())) {
                    throw new ServiceException("登录类型错误！");
                }
                return jxfwLogin(gdutDayWechatUser);
            }
            case EHALL_LOGIN -> {
                return ehallLogin(gdutDayWechatUser);
            }
        }
        return null;
    }


    /**
     * 本科教务管理
     *
     * @param gdutDayWechatUser 小程序登录VO
     */
    public abstract LoginDto jxfwLogin(GdutDayWechatUser gdutDayWechatUser);

    /**
     * 检查滑块
     *
     * @param gdutDayWechatUser
     * @param myokHttpClient
     */
    public void checkBlock(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) {
        HashMap<String, String> map = new HashMap<>(3);
        map.put("username", gdutDayWechatUser.getUser());
        map.put("_", Long.toString(System.currentTimeMillis()));
        RequestBody requestBody1 = JsoupUtils.map2PostUrlCodeString(map);
        //检查是否需要滑块认证
        Response response1 = okHttpUtils.postByFormUrl(myokHttpClient, CHECK_BLOCK_URL, requestBody1);
        JSONObject object;
        try {
            if (response1.code() != 200 || response1.body() == null) {
                log.error("统一认证滑块链接请求异常");
                throw new ServiceException("服务器未知错误，请联系开发者");
            }
            object = JSONObject.parseObject(response1.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean isNeed = (boolean) object.get("isNeed");
        if (isNeed) {
            throw new ServiceException("请到统一认证网址进行滑块认证再登录！！", HttpStatus.FORBIDDEN);
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
        this.checkBlock(gdutDayWechatUser, myokHttpClient);
        try {
            // 从登录的账号获得用户类型
            int userType = getUserInfoFromEhall(gdutDayWechatUser);
            if (userType < 0) {
                throw new ServiceException("未知异常，请联系开发者", HttpStatus.FORBIDDEN);
            }
            if (userType == UNDER_GRADUATE) {
                return this.underGraduateEhallLogin(gdutDayWechatUser, myokHttpClient);
            } else if (userType == GRADUATE) {
                graduateEhallLogin(gdutDayWechatUser, myokHttpClient);
                // 需要特殊处理cookie
                String cookieStr = OkHttpUtils.getCookieRemoveShortWEU(cookieJar.cookies);
                /**
                 *  留个demo给你，已经测试过其他接口，cookie可以正常使用
                 *  this.test(cookieStr);
                 */
                return new LoginDto(cookieStr, userType);
            } else if (userType == TEACHER) {
                ;
            }
            return new LoginDto("null", userType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 研究生登录
     *
     * @param gdutDayWechatUser
     * @param myokHttpClient
     * @return
     * @throws IOException
     */
    public abstract void graduateEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException;

    /**
     * 本科生登录
     *
     * @param gdutDayWechatUser
     * @param myokHttpClient
     * @return
     * @throws IOException
     */
    public abstract LoginDto underGraduateEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException;

    /**
     * 老师登录
     *
     * @param gdutDayWechatUser
     * @param myokHttpClient
     * @return
     * @throws IOException
     */
    public abstract LoginDto teacherEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException;


    /**
     * 从ehhall的接口获得用户信息，兼容三端
     *
     * @param gdutDayWechatUser 用户
     * @return 用户类型
     */
    public Integer getUserInfoFromEhall(GdutDayWechatUser gdutDayWechatUser) {
        String userNum = gdutDayWechatUser.getUser();
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
     * TODO 测试完请记得删除
     *
     * @param cookies
     */
    public void test(String cookies) {
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient();
        Response response = okHttpUtils.getAddCookie(okHttpClient, GRADUATE_USER_INFO, cookies);
        try {

            System.out.println("状态码：" + response.code());
            // 正常返回 {"data":[{"YXDM_DISPLAY":"计算机学院","YXYWMC":"School of Computers Science","XH":"2112205176","YXDM":"05","XM":"易铭"}]}
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            response.close();
        }
    }

}
