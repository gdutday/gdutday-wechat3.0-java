package com.gdutelc.utils;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/30 10:19
 * GdutDayCookieJar
 * 注意个别URL请求，这里可能有坑，okhttp默认把所有的cookie发送，如果出现同名的也会一起发出去
 */
public class GdutDayCookieJar implements CookieJar {
    public List<Cookie> cookies = new ArrayList<>();

    public HashMap<String, Cookie> cookieHashMap = new HashMap<>();

    /**
     * Fix cookie duplication issue,auto replaces old cookies
     * @param url url
     * @param cookies cookies
     */
    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        // 检查是否存在，自动替换原来旧的cookies
        for (Cookie cookie : cookies) {
            this.cookieHashMap.put(cookie.name(), cookie);
        }
        this.cookies = new ArrayList<>(this.cookieHashMap.values());
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        //有效的Cookie
        List<Cookie> validCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                //判断是否过期
                this.cookieHashMap.remove(cookie.name());
            } else if (cookie.matches(url)) {
                //匹配Cookie对应url
                validCookies.add(cookie);

            }
        }
        this.cookies = new ArrayList<>(this.cookieHashMap.values());
        //返回List<Cookie>让Request进行设置
        return validCookies;
    }


}
