package com.gdutelc.utils;

import com.gdutelc.common.constant.UrlConstant;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.riversun.okhttp3.OkHttp3CookieHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

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


    /***
     * 复用全局的okhttpClient
     * @return
     */
    public OkHttpClient makeOkhttpClient(GdutDayCookieJar cookieManager){
        // 需要复用改写cookie，添加 存储 后续会再优化 连接池和新建的问题
        return okHttpClient.newBuilder()
                .cookieJar(cookieManager)
                .build();
    }
    /**
     * 普通Get请求
     *
     * @param url
     * @return
     */
    public  Response get(OkHttpClient myOkHttpClient,String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
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
    public  Response get(OkHttpClient myOkHttpClient,String url,String jSessionId) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Cookie",jSessionId)
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
     * @return
     */
    public Response postByFormUrl(OkHttpClient myOkhttpClient,String url, RequestBody postData) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .post(postData)
                .build();
        try {
            return myOkhttpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }/***
     *
     * @param myOkhttpClient once
     * @param url url
     * @param postData postData
     * @return
     */
    public Response postByFormUrl(OkHttpClient myOkhttpClient,String url, RequestBody postData,String referer,String
                                  cookie) {
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/114.0")
                .header("Referer",referer)
                .header("Cookie",cookie)
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
     * @param html 页面内容
     * @return ture-正常，false-出现风控
     */
    public Boolean checkStatus(String html){
        if(html.contains("Please enable JavaScript and refresh the page")){
            return false;
        }
        return true;
    }
}
