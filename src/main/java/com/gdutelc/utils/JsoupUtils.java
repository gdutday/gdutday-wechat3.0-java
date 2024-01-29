package com.gdutelc.utils;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 21:35
 * JsoupUtils 解析内容
 */
public class JsoupUtils {

    /**
     * 把map对象转成能够发送application/x-www-form-urlencoded 类型的对象
     *
     * @param mapData map对象
     * @return RequestBody
     */
    public static RequestBody map2PostUrlCodeString(Map<String, String> mapData) {
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : mapData.keySet()) {
            builder.add(key, mapData.get(key));
        }
        return builder.build();
    }

    public static Document getUrlToDocument(String url, String jSessionId) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
                .header("Cookie",jSessionId)
                .header("Connection","keep-alive")
                .header("Referer","https://jxfw.gdut.edu.cn/login!welcome.action")
                .timeout(30000)
                .get();
    }
}
