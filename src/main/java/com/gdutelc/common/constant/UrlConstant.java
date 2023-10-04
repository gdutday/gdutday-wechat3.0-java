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
    //研究生获取授权链接
    public static final String JWT_URL = "https://authserver.gdut.edu.cn/authserver/login?service=http%3A%2F%2Fehall.gdut.edu.cn%2Fgsapp%2Fsys%2Fwdkbapp%2F*default%2Findex.do%3Famp_sec_version_%3D1%26gid_%3DanF6SjBZK2R1WVVlQ2l5cjJRSlBlckVnR05sSFVYRlVRWHVEYTVpT01WQ3Z0M01ENktsWXA5cHoveWxwNTdaSU9PMVRjWTF1Wk90Y3g4QjVOb3VhbWc9PQ%26EMAP_LANG%3Dzh%26THEME%3Dgolden%23%2Fxskcb";
    public static final String GRADUATE_KB_URL = "";
    public static final String GRADUATE_CJ_URL = "";

}
