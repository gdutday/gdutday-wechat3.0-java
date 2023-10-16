package com.gdutelc.utils;

import com.gdutelc.domain.GdutDayWechatUser;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 21:35
 * JsoupUtils 解析内容
 */
public class JsoupUtils {

    public static Document getUrlToDocument(String url,String jSessionId) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
                .header("Cookie",jSessionId)
                .header("Connection","keep-alive")
                .header("Referer","https://jxfw.gdut.edu.cn/login!welcome.action")
                .timeout(30000)
                .get();
    }


    /**
     * 从html页面和用户信息构造登录form
     *
     * @param html              获得登录页面
     * @param gdutDayWechatUser 用户个人信息
     * @return
     */
    public static RequestBody getLoginForm(String html, GdutDayWechatUser gdutDayWechatUser) throws UnsupportedEncodingException {
        Document doc = Jsoup.parse(html);
        // 复用 code
        String pwdEncryptSalt = null;
        Map<String, String> tempMap = new HashMap<>();

        //查找登录参数和加密密钥
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
        tempMap.put("password", LiUtils.cbcEncrypt(gdutDayWechatUser.getPassword(), pwdEncryptSalt));
        tempMap.put("username", gdutDayWechatUser.getUser());
        tempMap.put("", pwdEncryptSalt);
        tempMap.put("rememberMe", "true");
        // 登录前判断是否需要验证码
        tempMap.put("captcha", "");
        tempMap.put("_eventId", "submit");
        tempMap.put("cllt", "userNameLogin");
        tempMap.put("dllt", "generalLogin");
        tempMap.put("lt", "");
        return map2PostUrlCodeString(tempMap);
    }

    /**
     * 把map对象转成能够发送application/x-www-form-urlencoded 类型的对象
     *
     * @param mapData
     * @return
     * @throws UnsupportedEncodingException
     */
    public static RequestBody map2PostUrlCodeString(Map<String, String> mapData) {
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : mapData.keySet()) {
            builder.add(key, mapData.get(key));
        }
        return builder.build();
    }
}
