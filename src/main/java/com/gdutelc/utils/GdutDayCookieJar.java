package com.gdutelc.utils;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/30 10:19
 * GdutDayCookieJar
 */
public class GdutDayCookieJar implements CookieJar {
    private final List<Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies.addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> validCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.matches(url)) {
                validCookies.add(cookie);
            }
        }
        return validCookies;
    }
}
