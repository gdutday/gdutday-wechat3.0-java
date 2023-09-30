package com.gdutelc.common.config;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 21:46
 * OkhttpInterceptor
 */
class OkhttpInterceptor implements Interceptor {

    private int maxRentry;// 最大重试次数

    public OkhttpInterceptor(int maxRentry) {
        this.maxRentry = maxRentry;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(OkhttpInterceptor.class);

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        /* 递归 4次下发请求，如果仍然失败 则返回 null ,但是 intercept must not return null.
         * 返回 null 会报 IllegalStateException 异常
         * */
        return retry(chain, 0);
    }

    Response retry(Chain chain, int retryCent) {
        Request request = chain.request();
        Response response = null;
        try {
            LOGGER.info("第" + (retryCent + 1) + "次执行下发NEF请求.");
            response = chain.proceed(request);
        } catch (Exception e) {
            if (maxRentry > retryCent) {
                return retry(chain, retryCent + 1);
            }
        } finally {
            return response;
        }
    }
}
