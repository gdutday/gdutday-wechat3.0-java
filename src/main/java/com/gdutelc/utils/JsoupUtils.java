package com.gdutelc.utils;

import okhttp3.FormBody;
import okhttp3.RequestBody;

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
}
