package com.gdutelc.service.impl;

import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.service.adapter.AbstractLoginAdapter;
import com.gdutelc.utils.GdutDayCookieJar;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.OkHttpUtils;
import jakarta.annotation.Resource;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 01:47
 * LoginServiceImpl
 */
@Service
public class LoginServiceImpl extends AbstractLoginAdapter {

    @Resource
    private OkHttpUtils okHttpUtils;

    @Override
    public LoginDto jxfwLogin(GdutDayWechatUser gdutDayWechatUser) {
        /**
         * 1.本科登录
         * 2.自动身份
         * 3.通过认证
         */
        return null;
    }

    @Override
    public LoginDto ehallLogin(GdutDayWechatUser gdutDayWechatUser) {
        /**
         * 全局共用一个okhttp，但是每次都复用okhttpclinent,
         * 严格使用工具new okhttp,登录的时候直接传入new的cookie,最后记得释放和清空
         * 除登录外其他可直接复用全局的，需要先清除
         * 1.拿到页面参数
         * 2.通过登录
         * 3.身份校验
         * 4.通过认证
         */
        try {
            GdutDayCookieJar cookieJar = new GdutDayCookieJar();
            OkHttpClient myokHttpClient = okHttpUtils.makeOkhttpClient(cookieJar);
            Response response = okHttpUtils.get(myokHttpClient, UrlConstant.GRADUATE_LOGIN);
            //response.body().string();
            assert response.body() != null;
            String html = response.body().string();
            if (!okHttpUtils.checkStatus(html)) {
                return null;
            }
            response.close();
            RequestBody requestBody = JsoupUtils.getLoginForm(html, gdutDayWechatUser);

            // 发送登录请求, 待完善...
            response = okHttpUtils.postByFormUrl(myokHttpClient, UrlConstant.GRADUATE_LOGIN, requestBody);
            // 从cookieJar 里面拿到登录过的cookies，然后返回
            Headers headers = response.headers();
            // 返回DTO还没统一，先全部丢到AjaxResult
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
