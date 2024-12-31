package com.gdutelc.utils;

import jakarta.annotation.Resource;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 22:59
 * OkHttpUtils, 仅在本项目使用，未对于其他情况进行更加全面的封装
 */
@Component
public class OkHttpUtils {

    // 全局单例化，初始化见OkhttpConfiguration
    @Resource
    private OkHttpClient okHttpClient;

    // 正则匹配学号
    private static final String pattern = "\"uid\":\\s*\"(\\d+)\"";

    /***
     * 复用全局的okhttpClient
     * @return okhttpClient
     */
    public OkHttpClient makeOkhttpClient(CookieJar cookieJar) {
        return okHttpClient.newBuilder()
                .cookieJar(cookieJar)
                .followRedirects(false) // 取消自动重定向，登录的时候手动handle
                .build();
    }

    public OkHttpClient makeOkhttpClientAuto(CookieJar cookieJar) {
        return okHttpClient.newBuilder()
                .cookieJar(cookieJar)
                .followRedirects(true) // 取消自动重定向，登录的时候手动handle
                .build();
    }
    /**
     * 朴素的okhttpClient，后续需要统一优化
     * 后续需要优化 连接池和新建的问题
     *
     * @return
     */
    public OkHttpClient makeOkhttpClient() {
        return okHttpClient.newBuilder()
                .build();
    }

    /**
     * 有代理的okhttpClient，用于调试抓包
     *
     * @return
     */
    public OkHttpClient makeOkhttpClientByProxy(CookieJar cookieManager) {
        return okHttpClient.newBuilder()
                .cookieJar(cookieManager)
                .followRedirects(false)
                .build();
    }

    /**
     * 重定向手动挡
     *
     * @param cookieManager
     * @param followRedirects
     * @return
     */
    public OkHttpClient makeOkhttpClient(CookieJar cookieManager, boolean followRedirects) {
        return okHttpClient.newBuilder()
                .cookieJar(cookieManager)
                .followRedirects(followRedirects)
                .build();
    }


    /**
     * 普通Get请求
     *
     * @param url url
     * @return Response
     */
    public Response get(OkHttpClient myOkHttpClient, String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .build();
        try {
            Response response = myOkHttpClient.newCall(request).execute();
            //response.body().string();
            assert response.body() != null;
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 请求带cookie
     *
     * @param myOkHttpClient okHttpClient
     * @param url          url
     * @param cookies     cookies
     * @return Response
     */
    public Response getAddCookie(OkHttpClient myOkHttpClient, String url, String cookies) {
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", cookies)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .build();
        try {
            Response response = myOkHttpClient.newCall(request).execute();
            assert response.body() != null;
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 本科端使用
     *
     * @param myOkHttpClient okHttpClient
     * @param url           url
     * @param jSessionId   jSessionId
     * @return Response
     */
    public Response get(OkHttpClient myOkHttpClient, String url, String jSessionId) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Cookie", jSessionId)
                .build();
        try {
            Response response = myOkHttpClient.newCall(request).execute();
            //response.body().string();
            assert response.body() != null;
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Response get(OkHttpClient myOkHttpClient, String url, String jSessionId,String ref) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Cookie", jSessionId)
                .header("Referer", ref)
                .build();
        try {
            Response response = myOkHttpClient.newCall(request).execute();
            //response.body().string();
            assert response.body() != null;
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param myOkhttpClient once
     * @param url url
     * @param postData postData
     * @return Response
     */
    public Response postByFormUrl(OkHttpClient myOkhttpClient, String url, RequestBody postData) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                // 奇怪的埋点，它要就给呗~
                .header("sec-ch-ua","\"(Not(A:Brand\";v=\"99\", \"Google Chrome\";v=\"129\", \"Chromium\";v=\"129\"")
                .header("sec-ch-ua-full-version-list","\"(Not(A:Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"129\", \"Chromium\";v=\"129\"")
                .header("sec-ch-ua-mobile","?0")
                .header("sec-ch-ua-platform","\"Linux\"")
                .post(postData)
                .build();
        try {
            return myOkhttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param myOkhttpClient once
     * @param url url
     * @param postData postData
     * @return Respone
     */
    public Response postByFormUrlWithCookie(OkHttpClient myOkhttpClient, String url, RequestBody postData, String cookie) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Cookie", cookie)
                .post(postData)
                .build();
        try {
            return myOkhttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /***
     *
     * @param myOkhttpClient once
     * @param url url
     * @param postData postData
     * @return Response
     */
    public Response postByFormUrl(OkHttpClient myOkhttpClient, String url, RequestBody postData, String referer, String
            cookie) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Referer", referer)
                .header("Cookie", cookie)
                .post(postData)
                .build();
        try {
            return myOkhttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     *
     * @param myOkhttpClient once
     * @param url url
     * @param postData postData
     * @return Response
     */
    public Response postByFormUrl(OkHttpClient myOkhttpClient, String url, RequestBody postData, String referer) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Referer", referer)
                .post(postData)
                .build();
        try {
            return myOkhttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查风控
     *
     * @param html 页面内容
     * @return ture-正常，false-出现风控
     */
    public Boolean checkStatus(String html) {
        return !html.contains("Please enable JavaScript and refresh the page");
    }

    /***
     * 从List里面获取cookie, 移除短的_WEU，短_WEU会导致403，仅在研究生登录使用
     * @param cookies cookie
     * @return Processed cookies
     */
    public static String getCookieRemoveShortWEU(List<Cookie> cookies) {
        StringBuilder cookieStr = new StringBuilder();
        Cookie tempWEU = null;
        for (Cookie cookie : cookies) {
            // 只保留cookie里面长的_WEU,
            if (cookie.name().equals("_WEU")) {
                if (tempWEU == null) {
                    tempWEU = cookie;
                    continue;
                } else if (tempWEU.value().length() < cookie.value().length()) {
                    tempWEU = cookie;
                    continue;
                }
            }
            cookieStr.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }
        if (tempWEU != null) {
            cookieStr.append(tempWEU.name()).append("=").append(tempWEU.value()).append(";");
        }
        return cookieStr.toString();
    }

    /**
     * cookieJar to cookie string
     * @param cookies List<Cookie></>
     * @return string cookie
     */
    public static String getCookies(List<Cookie> cookies) {
        StringBuilder cookieStr = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieStr.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }
        return cookieStr.toString();
    }


    /**
     * Obtain uid from the response, which is the student ID
     * @param html response.body().string()
     * @return student ID
     */
    public static String getUid(String html) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(html);
        if (matcher.find()) {
            return  matcher.group(1);
        } else {
            return null;
        }

    }


}

