package com.gdutelc.common.constant;

/**
 * @author Ymri
 * @version 1.0
 * @since 2023/9/29 16:25
 * UrlConstant
 * 各种登录URL
 */
public class UrlConstant {

    //统一登陆检查是否需要滑块验证
    public static String CHECK_BLOCK_URL = "https://authserver.gdut.edu.cn/authserver/checkNeedCaptcha.htl";

    // 本科生登录链接
    public static final String UNDER_GRADUATE_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=https%3A%2F%2Fjxfw.gdut.edu.cn%2Fnew%2FssoLogin";
    //本科生获取学期链接
    public static final String UNDER_CLAZZ_TERM = "https://jxfw.gdut.edu.cn/xsbjkbcx!xsbjkbMain.action";
    public static final String UNDER_CLAZZ = "https://jxfw.gdut.edu.cn/xsgrkbcx!getDataList.action";
    // 研究生登录链接
    public static final String GRADUATE_EHALL_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=http%3A%2F%2Fehall.gdut.edu.cn%2Fgsapp%2Fsys%2Fwdkbapp%2F*default%2Findex.do%3Famp_sec_version_%3D1%26gid_%3DanF6SjBZK2R1WVVlQ2l5cjJRSlBlckVnR05sSFVYRlVRWHVEYTVpT01WQ3Z0M01ENktsWXA5cHoveWxwNTdaSU9PMVRjWTF1Wk90Y3g4QjVOb3VhbWc9PQ%26EMAP_LANG%3Dzh%26THEME%3Dgolden%23%2Fxskcb";
    // 研究生学期信息
    public static final String GRADUATE_SEMESTER = "http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/kfdxnxqcx.do";
    // 研究生课表接口，需要先拿到学期信息
    public static final String GRADUATE_KB = "http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/xspkjgcx.do?XNXQDM=20221&*order=<*order>";
    // 考试成绩
    public static final String GRADUATE_EXAM = "https://ehall.gdut.edu.cn/gsapp/sys/wdcjapp/modules/wdcj/xscjcx.do";

    // 本科教务系统登录
    public static final String JXFW_LOGIN = "https://jxfw.gdut.edu.cn/new/login";

    // 从ehall 获得用户个人信息，用来测试是否登录成功
    public static final String GRADUATE_USER_INFO = "http://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/wdkcb/initXsxx.do?XH=";


    // 登录ehall大厅 pre登录
     public static final String EHALL_URL = "https://authserver.gdut.edu.cn/authserver/login?service=https%3A%2F%2Fehall.gdut.edu.cn%3A443%2Flogin%3Fservice%3Dhttps%3A%2F%2Fehall.gdut.edu.cn%2Fnew%2Findex.html";

    // ehall的个人信息接口，用来获取正确的学号
    public static final String EHALL_USER_INFO = "https://authserver.gdut.edu.cn/personalInfo/common/getUserConf";

    // 后续补充这里即可
    public static final String TEACHER__EHALL_LOGIN = "https://authserver.gdut.edu.cn/";

}
