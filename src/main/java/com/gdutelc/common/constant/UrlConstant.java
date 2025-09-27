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
    public static final String UNDER_GRADUATE_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=https://jxfw.gdut.edu.cn/new/ssoLogin";
    //本科生获取学期链接
    public static final String UNDER_CLAZZ_TERM = "https://jxfw.gdut.edu.cn/xsksap!ksapList.action";
    public static final String UNDER_CLAZZ = "https://jxfw.gdut.edu.cn/xsgrkbcx!getDataList.action";
    // 本科生获得成绩接口
    public static final String UNDER_EXAME_SCORE = "https://jxfw.gdut.edu.cn/xskccjxx!getDataList.action";
    public static final String UNDER_REFER = "https://jxfw.gdut.edu.cn/";

    //    public static final String GRADUATE_REFER = "https://yjsxt.gdut.edu.cn/";
    // 研究生登录链接
    public static final String GRADUATE_EHALL_LOGIN = "https://authserver.gdut.edu.cn/authserver/login?service=https%3A%2F%2Fyjsxt.gdut.edu.cn%2Fgsapp%2Fsys%2Fyjsemaphome%2Fportal%2Findex.do";

    // 成绩登录授权
    public static final String GRADUATE_EHALL_SCORE_LOGIN = "https://yjsxt.gdut.edu.cn/gsapp/sys/wdcjapp/*default/index.do#/wdcj";

    // 课表登录授权
    public static  final  String GRADUATE_KB_LOGIN = "https://yjsxt.gdut.edu.cn/gsapp/sys/wdkbapp/*default/index.do";
    // 研究生学期信息
    public static final String GRADUATE_SEMESTER = "https://yjsxt.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/kfdxnxqcx.do";

    // 研究生课表接口，需要先拿到学期信息
    public static final String GRADUATE_KB = "https://yjsxt.gdut.edu.cn/gsapp/sys/wdkbapp/modules/xskcb/xspkjgcx.do?XNXQDM=20221&*order=<*order>";
    // 考试成绩
    public static final String GRADUATE_EXAM = "https://yjsxt.gdut.edu.cn/gsapp/sys/wdcjapp/modules/wdcj/xscjcx.do";


    // 本科教务系统登录
    public static final String JXFW_LOGIN = "https://jxfw.gdut.edu.cn/new/login";

    // 从ehall 获得用户个人信息，用来测试是否登录成功
    public static final String GRADUATE_USER_INFO = "https://ehall.gdut.edu.cn/gsapp/sys/wdkbapp/wdkcb/initXsxx.do?XH=";


    // 登录ehall大厅 pre登录
    public static final String EHALL_URL = "https://authserver.gdut.edu.cn/authserver/login?type=userNameLogin";

    // ehall的个人信息接口，用来获取正确的学号
    public static final String EHALL_USER_INFO = "https://authserver.gdut.edu.cn/personalInfo/common/getUserConf";

    // 后续补充这里即可
    public static final String TEACHER__EHALL_LOGIN = "https://authserver.gdut.edu.cn/";

}
