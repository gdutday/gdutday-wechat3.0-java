package com.elc.constant;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2023/09/20/21:17
 * @Description: 所有发请求的url
 */
public class RequestUrl {
    //统一认证登录url
    public static String LOGIN_URL = "https://authserver.gdut.edu.cn/authserver/login?service=http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/*default/index.do#/xskcb";
    //检查是否需要滑块验证
    public static String CHECK_BLOCK_URL = "https://authserver.gdut.edu.cn/authserver/checkNeedCaptcha.htl";
}
