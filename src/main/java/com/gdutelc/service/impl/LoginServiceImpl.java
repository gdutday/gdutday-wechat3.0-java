package com.gdutelc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gdutelc.common.constant.UrlConstant;
import com.gdutelc.domain.DTO.LoginDto;
import com.gdutelc.domain.GdutDayWechatUser;
import com.gdutelc.framework.common.HttpStatus;
import com.gdutelc.framework.exception.ServiceException;
import com.gdutelc.service.adapter.AbstractLoginAdapter;
import com.gdutelc.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        map.put("account", gdutDayWechatUser.getUser());
        map.put("pwd", gdutDayWechatUser.getPassword());
        map.put("verifycode", gdutDayWechatUser.getCode());
        RequestBody requestBody = JsoupUtils.map2PostUrlCodeString(map);
        Request request = new Request.Builder()
                .header("Cookie", gdutDayWechatUser.getJSessionId())
                .post(requestBody)
                .url(JXFW_LOGIN)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            assert response.body() != null;
            String bodyStr = response.body().string();
            JSONObject object = JSONObject.parseObject(bodyStr);
            if (response.code() != 200 || object.getInteger("code") != 0) {
                String message = object.getString("message");
//                throw new ServiceException(message.equals("连接已过期") ? "验证码过期" : message
//                        , HttpStatus.f011);
                if (message.equals("验证码不正确")) {
                    throw new ServiceException(message, HttpStatus.f011);
                } else {
                    throw new ServiceException("账号或密码错误", HttpStatus.f005);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new LoginDto(gdutDayWechatUser.getJSessionId(), gdutDayWechatUser.getUserType());
    }

    @Override
    public void preLogin(GdutDayWechatUser gdutDayWechatUser, OkHttpClient myokHttpClient) {
        String html;
        try (Response response = okHttpUtils.get(myokHttpClient, EHALL_URL)) {
            assert response.body() != null;
            html = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!okHttpUtils.checkStatus(html)) {
            throw new ServiceException("登录异常，请重新登录", HttpStatus.f003);
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

        try (Response responses = okHttpUtils.postByFormUrl(myokHttpClient, EHALL_URL, requestBody)) {
            // 手动重定向认证
            debugRedirect(myokHttpClient, responses);
//            log.info("{}",responses.body());
            // 修改不允许重定向，手动挨个测试
        } catch (ServiceException e) {
            throw new ServiceException("账号或密码错误", HttpStatus.f005);
        } catch (Exception e) {
            throw new ServiceException("内部异常！", HttpStatus.f5001);
        }
    }

    @Override
    public void postLoginByUrl(String url, OkHttpClient okHttpClient) {
        // 构造空的请求体
        try (Response response = okHttpUtils.postByFormUrl(okHttpClient, url, RequestBody.create(new byte[0]))) {
            if (response.code() != 200) {
                throw new ServiceException("登录异常，请重新登录！", HttpStatus.f003);
            }
            if (response.body() != null) {
                response.body().close();
            }
        } catch (ServiceException e) {
            throw new ServiceException("登录异常，请重新登录！", HttpStatus.f003);
        } catch (Exception e) {
            throw new ServiceException("内部异常！", HttpStatus.f5001);
        }


    }

    private void debugRedirect(OkHttpClient client, Response response) throws IOException {
        int responseCode = response.code();
        // 需要发送埋点 追踪 请求
        String location = response.header("Location");
//        log.debug("Response Code: {}", location);
        int maxTime = 0;
        // 在请求都上添加 https://ehall.gdut.edu.cn:443/login?service=https://ehall.gdut.edu.cn/new/index.html&ticket=
        while (responseCode == 301 || responseCode == 302 || responseCode == 303 || responseCode == 307 || responseCode == 308) {
//            System.out.println("Redirecting to: " + location);

            Request newRequest = new Request.Builder()
                    .url(location).header("Host", "authserver.gdut.edu.cn")
                    .header("Pragma", "no-cache")
                    .header("Referer", "https://authserver.gdut.edu.cn/authserver/login?type=userNameLogin")
                    .header("sec-ch-ua", "\"(Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"130\", \"Chromium\";v=\"130\"")
                    .header("sec-ch-ua-full-version-list", "\"(Not(A:Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"130\", \"Chromium\";v=\"130\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Linux\"")
                    .header("sec-fetch-dest", "document")
                    .header("sec-fetch-mode", "navigate")
                    .header("sec-fetch-site", "same-site")
                    .header("sec-fetch-user", "?1")
                    .header("upgrade-insecure-requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6613.35 Safari/537.36")
//                    .headers(headers)
                    .get() // 使用 GET 方法
                    .build();

            response.close(); // 关闭上一个响应
            //
            response = client.newCall(newRequest).execute();
            responseCode = response.code();
//            log.debug("Response Code: {}", responseCode);
//            log.debug("Response Body: {}", response.body().string());
            location = response.header("Location");
            // 最后会无限重定向，因为nginx一直永远返回302
            if(StringUtils.isEmpty(location)||maxTime>3){
                break;
            }
            maxTime++;
        }
    }
}
