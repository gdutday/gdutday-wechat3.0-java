package com.gdutelc.common.config;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 19:34
 * Okhtt配置和Bean,使用单例模式，防止连接过多没有释放出现OOM
 */
@Configuration
public class OkhttpConfiguration {


    private static final Logger LOGGER = LoggerFactory.getLogger(OkhttpConfiguration.class);
    private static final int maxIdleConnections = 128;

    private static final int keepAliveDuration = 60;

    private static final int readTimeout = 30;
    private static final int writeTimeout = 30;

    private static final int MAXRENTRY = 2; // 密码错误会多次重试

    private static final int maxRequests = 1024;
    // 设置线程
    private static ExecutorService executorService = new ThreadPoolExecutor(maxRequests, 1024, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    /**
     * IDEA 由于版本问题可能会出现异常，忽视即可
     *
     * @return OkHttpClient
     */
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .retryOnConnectionFailure(false) //是否开启缓存
                .connectionPool(pool())
                .dispatcher(new Dispatcher(executorService))
//                .followRedirects(false) // 默认自动重定向； 禁止重定向，方便获取cookies
                .addInterceptor(new OkhttpInterceptor(MAXRENTRY)) // 自动重试
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
        okHttpClient.dispatcher().setMaxRequests(maxRequests);
        return okHttpClient;
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.warn(e.getMessage());
        }
        return null;
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
    }

}
