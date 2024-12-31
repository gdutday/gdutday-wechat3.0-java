package com.gdutelc.utils;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
            } else{
                //匹配Cookie对应url
                //  if (cookie.matches(url))
                validCookies.add(cookie);

            }
        }
        validCookies.add(Cookie.parse(url, "zg_did=%7B%22did%22%3A%20%2218b228c5a34de9-0425760ef7a351-28382846-1fa400-18b228c5a3543c%22%7D;"));
        validCookies.add(Cookie.parse(url, "zg_=%7B%22sid%22%3A%201708345090339%2C%22updated%22%3A%201708345090342%2C%22info%22%3A%201708345090341%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22ehall.gdut.edu.cn%22%2C%22cuid%22%3A%20%114514%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22firstScreen%22%3A%201708345090339%7D;"));
        // 特殊构造一个Cookie 貌似没用，丢这里，不管
        if(StringUtils.isNotEmpty(url.query())&&url.query().contains("ticket")){
            // 唯一标识，不晓得如何生成
            validCookies.add(Cookie.parse(url, "zg_did=%7B%22did%22%3A%20%2218b228c5a34de9-0425760ef7a351-28382846-1fa400-18b228c5a3543c%22%7D;"));
            validCookies.add(Cookie.parse(url, "gwroute-casp-portal=651d1cdb3d8f38f59dba6f714441e0dd;"));
            validCookies.add(Cookie.parse(url, "COM.WISEDU.CASP.IS_RANDOM=0;"));
            validCookies.add(Cookie.parse(url, "COM.WISEDU.CASP.SITEWID=-100;"));
            validCookies.add(Cookie.parse(url, "route=8d8d8138c281db2b67d4236b800791f9;"));
        }
        this.cookies = new ArrayList<>(this.cookieHashMap.values());
        //返回List<Cookie>让Request进行设置
//        log.info("Cookies:{}", validCookies);
        return validCookies;
    }


}
