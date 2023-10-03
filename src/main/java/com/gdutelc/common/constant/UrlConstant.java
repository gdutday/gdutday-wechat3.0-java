package com.gdutelc.common.constant;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 16:25
 * UrlConstant
 * 各种登录URL
 */
public class UrlConstant {

    public static final String JGFW = "";
    public static final String EHALL_LOGIN = "https://authserver.gdut.edu.cn/authserver/login";

    //统一登陆检查是否需要滑块验证
    public static String CHECK_BLOCK_URL = "https://authserver.gdut.edu.cn/authserver/checkNeedCaptcha.htl";

    // 本科生登录链接
    public static final String UNDER_GRADUATE_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=https%3A%2F%2Fjxfw.gdut.edu.cn%2Fnew%2FssoLogin";
    // 研究生登录链接
    public static final String GRADUATE_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/*default/index.do#/xskcb";
    public static final String GRADUATE_KB_URL = "";
    public static final String GRADUATE_CJ_URL = "";

}
