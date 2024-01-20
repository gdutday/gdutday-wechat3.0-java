package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.adapter.AbstractLoginAdapter;
import com.gdutelc.utils.GdutDayCookieJar;
import com.gdutelc.utils.JsoupUtils;
import com.gdutelc.utils.LiUtils;
import com.gdutelc.utils.OkHttpUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.gdutelc.common.constant.UrlConstant.GRADUATE_EHALL_LOGIN;
import static com.gdutelc.common.constant.UrlConstant.JXFW_LOGIN;

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
        map.put("account", gdutDayWechatUser.getUser());
        map.put("pwd", gdutDayWechatUser.getPassword());
        map.put("verifycode", gdutDayWechatUser.getCode());
        RequestBody requestBody = JsoupUtils.map2PostUrlCodeString(map);
        Request request = new Request.Builder()
                .header("Cookie", gdutDayWechatUser.getJSessionId())
                .post(requestBody)
                .url(JXFW_LOGIN)
                .build();
        Response response;
        try {
            response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            String bodyStr = response.body().string();
            JSONObject object = JSONObject.parseObject(bodyStr);
            if (response.code() != 200 || object.getInteger("code") != 0) {
                String message = object.getString("message");
                throw new ServiceException(message.equals("连接已过期") ? "验证码过期" : message
                        , response.code() == 200 ? 400 : response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new LoginDto(gdutDayWechatUser.getJSessionId(), gdutDayWechatUser.getUserType());
    }

    @Override
    public void preLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) {
        Response response = okHttpUtils.get(myokHttpClient, UrlConstant.EHALL_URL);
        assert response.body() != null;
        String html;
        try {
            html = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.close();
        if (!okHttpUtils.checkStatus(html)) {
            throw new ServiceException("登录异常，请重新登录", response.code());
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
        tempMap.put("", pwdEncryptSalt);
        tempMap.put("rememberMe", "true");
        tempMap.put("captcha", "");
        tempMap.put("username", gdutDayWechatUser.getUser());
        tempMap.put("password", LiUtils.cbcEncrypt(gdutDayWechatUser.getPassword(), pwdEncryptSalt));
        RequestBody requestBody = JsoupUtils.map2PostUrlCodeString(tempMap);
        // 由于存在账号密码输入错误，所以只重试1次，不然错误密码次数过多很快就要滑块了
        response = okHttpUtils.postByFormUrl(myokHttpClient, GRADUATE_EHALL_LOGIN, requestBody);
        // 这里会自动多次重定向拿到ehall的cookie
        if (response.code() != 200) {
            throw new ServiceException("账号或密码错误", response.code());
        }
        response.close();
    }

    @Override
    public void postLoginByUrl(String url, OkHttpClient okHttpClient) {
        // 构造空的请求体
        Response response = okHttpUtils.postByFormUrl(okHttpClient, url, RequestBody.create(new byte[0]));
        if (response.code() != 200) {
            throw new ServiceException("登录异常，请重新登录", response.code());
        }
    }


}
