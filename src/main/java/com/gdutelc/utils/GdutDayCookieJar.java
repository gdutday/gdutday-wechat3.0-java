package com.gdutelc.utils;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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


    @Override
    public void saveFromResponse(HttpUrl url, @NotNull List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        //过期的Cookie
        List<Cookie> invalidCookies = new ArrayList<>();
        //有效的Cookie
        List<Cookie> validCookies = new ArrayList<>();
        Cookie tempCookie =null;
        for (Cookie cookie : cookies) {
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                //判断是否过期
                invalidCookies.add(cookie);
            } else if (cookie.matches(url)) {
                //匹配Cookie对应url
                validCookies.add(cookie);
            }
        }
        //缓存中移除过期的Cookie
        cookies.removeAll(invalidCookies);
        //返回List<Cookie>让Request进行设置
        return validCookies;
    }


}
