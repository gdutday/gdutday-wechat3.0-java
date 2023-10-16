package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.framework.domain.AjaxResult;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.adapter.AbstractLoginAdapter;
import com.gdutelc.utils.GdutDayCookieJar;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.LiUtils;
import com.gdutelc.utils.OkHttpUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import static com.gdutelc.common.constant.UrlConstant.*;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 01:47
 * LoginServiceImpl
 */
@Slf4j
@Service
public class LoginServiceImpl extends AbstractLoginAdapter {

    @Resource
    private OkHttpUtils okHttpUtils;


    @Override
    public LoginDto jxfwLogin(GdutDayWechatUser gdutDayWechatUser) {
        GdutDayCookieJar gdutDayCookieJar = new GdutDayCookieJar();
        OkHttpClient okHttpClient = okHttpUtils.makeOkhttpClient(gdutDayCookieJar);
        HashMap<String, String> map = new HashMap<>(6);
        map.put("account",gdutDayWechatUser.getUser());
        map.put("pwd",gdutDayWechatUser.getPassword());
        map.put("verifycode",gdutDayWechatUser.getCode());
        RequestBody requestBody = JsoupUtils.map2PostUrlCodeString(map);
        Request request = new Request.Builder()
                .header("Cookie", gdutDayWechatUser.getJSessionId())
                .post(requestBody)
                .url("https://jxfw.gdut.edu.cn/new/login")
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(response.code()!=200){
            throw new ServiceException("账号，密码或验证码错误",response.code());
        }
        return new LoginDto("大学城校区",gdutDayWechatUser.getJSessionId(),gdutDayWechatUser.getUserType());
    }

    @Override
    public LoginDto graduateEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException {
        Response response = okHttpUtils.get(myokHttpClient, "http://ehall.gdut.edu.cn/login?service=http://ehall.gdut.edu.cn/new/index.html");
        //response.body().string();
        assert response.body() != null;
        String html = response.body().string();
        if (!okHttpUtils.checkStatus(html)) {
            return null;
        }
        response.close();
        RequestBody requestBody = JsoupUtils.getLoginForm(html, gdutDayWechatUser);
        // 发送登录请求, 待完善...
        response = okHttpUtils.postByFormUrl(myokHttpClient, "https://authserver.gdut.edu.cn/authserver/login?service=http%3A%2F%2Fehall.gdut.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.gdut.edu.cn%2Fnew%2Findex.htm", requestBody);
        // 从cookieJar 里面拿到登录过的cookies，然后返回
        // 从cookieJar 里面拿到登录过的cookies，然后返回
        if(response.code()!=200){
            throw new ServiceException("账号或密码错误", response.code());
        }
        List<Cookie> cookies = myokHttpClient.cookieJar().loadForRequest(HttpUrl.parse(UNDER_GRADUATE_LOGIN));
        String cookieStr = "";
        for (Cookie cookie : cookies) {
            cookieStr += cookie.name()+":"+cookie.value()+";";
        }
        // 返回DTO还没统一，先全部丢到AjaxResult
        return new LoginDto("大学城校区",cookieStr,gdutDayWechatUser.getUserType());
    }

    @Override
    public LoginDto underGraduateEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException {
        Response response = okHttpUtils.get(myokHttpClient, "https://jxfw.gdut.edu.cn/new/ssoLogin");
        assert response.body() != null;
        String html = response.body().string();
        if (!okHttpUtils.checkStatus(html)) {
            return null;
        }
        Document doc = Jsoup.parse(html);
        String pwdEncryptSalt = null;
        Map<String, String> tempMap = new HashMap<>(10);
        // 查找元素，例如查找id为"pwdFromId"的元素
        Elements pwdFromIdElements = doc.select("#pwdFromId");
        // 遍历找到的元素
        for (Element pwdFromIdElement : pwdFromIdElements) {
            // 查找该元素下的所有类型为"hidden"的input元素
            Elements hiddenInputs = pwdFromIdElement.select("input[type=hidden]");
            // 遍历这些input元素并获取它们的属性值
            for (Element hiddenInput : hiddenInputs) {
                String name = hiddenInput.attr("name");
                String value = hiddenInput.attr("value");
                if (!name.isEmpty()) {
                    tempMap.put(name, value);
                }
                // 查找并保存盐值（"pwdEncryptSalt"）
                String id = hiddenInput.attr("id");
                if ("pwdEncryptSalt".equals(id)) {
                    pwdEncryptSalt = value;
                }
            }
        }
        tempMap.put("",pwdEncryptSalt);
        tempMap.put("rememberMe", "true");
        tempMap.put("captcha", "");
        tempMap.put("username",gdutDayWechatUser.getUser());
        tempMap.put("password", LiUtils.cbcEncrypt(gdutDayWechatUser.getPassword(),pwdEncryptSalt));
        RequestBody requestBody = JsoupUtils.map2PostUrlCodeString(tempMap);
        // 发送登录请求, 待完善...
        Response response1 = okHttpUtils.postByFormUrl(myokHttpClient, UNDER_GRADUATE_LOGIN, requestBody);
        // 从cookieJar 里面拿到登录过的cookies，然后返回
        String result = response1.body().string();
        if(response1.code()!=200){
            throw new ServiceException("账号或密码错误",response1.code());
        }
        List<Cookie> cookies = myokHttpClient.cookieJar().loadForRequest(HttpUrl.parse("https://authserver.gdut.edu.cn/authserver/login?service=https%3A%2F%2Fjxfw.gdut.edu.cn%2Fnew%2FssoLogin"));
        String cookieStr = "";
        for (Cookie cookie : cookies) {
            cookieStr += cookie.name()+":"+cookie.value()+";";
        }
        // 返回DTO还没统一，先全部丢到AjaxResult
        return new LoginDto("大学城校区",cookieStr,gdutDayWechatUser.getUserType());
    }

    @Override
    public LoginDto teacherEhallLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) throws IOException {
        return null;
    }


}
