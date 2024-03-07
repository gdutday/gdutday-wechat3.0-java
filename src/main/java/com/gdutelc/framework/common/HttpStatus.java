package com.gdutelc.framework.common;

/**
 * 返回状态码
 * 
 * @author Ymri
 */
public class HttpStatus
{
    /**
     * 操作成功
     */
    public static final int SUCCESS = 200;

    /**
     * 对象创建成功
     */
    public static final int CREATED = 201;

    /**
     * 请求已经被接受
     */
    public static final int ACCEPTED = 202;

    /**
     * 操作已经执行成功，但是没有返回数据
     */
    public static final int NO_CONTENT = 204;

    /**
     * 资源已被移除
     */
    public static final int MOVED_PERM = 301;
    public static final int REDIRECT = 302;

    /**
     * 重定向
     */
    public static final int SEE_OTHER = 303;

    /**
     * 资源没有被修改
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 访问受限，授权过期
     */
    public static final int FORBIDDEN = 403;

    /**
     * 资源，服务未找到
     */
    public static final int NOT_FOUND = 404;

    /**
     * 不允许的http方法
     */
    public static final int BAD_METHOD = 405;

    /**
     * 资源冲突，或者资源被锁
     */
    public static final int CONFLICT = 409;

    /**
     * 不支持的数据，媒体类型
     */
    public static final int UNSUPPORTED_TYPE = 415;

    /**
     * 系统内部错误
     */
    public static final int ERROR = 500;

    /**
     * 接口未实现
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * 系统警告消息
     */
    public static final int WARN = 601;

    /**
     * * 请求验证码失败，请重试！ 4001
     * * 请检查请求参数！ 4002
     * * 登录类型错误！4003
     * * 登录异常，请重新登录！（出现风控页面） 4004
     * * 账号或密码错误 4005
     * * 请求课表异常，请重试！(请求页面出现问题) 4006
     * * 身份信息过期，请重新登录！ 4007
     * * 请求成绩异常，请重试！ 4008
     * * 请求考试安排异常，请重试！4009
     * * Please enter the correct date！修改课程格式错误）4010
     * * 验证码过期 4011
     * * json解析错误，可能是cookie错误或过期，请重新登录
     */
    public static final int f001=4001;

    public static final int f002=4002;
    public static final int f003=4003;
    public static final int f004=4004;
    public static final int f005=4005;
    public static final int f006=4006;
    public static final int f007=4007;
    public static final int f008=4008;
    public static final int f009=4009;
    public static final int f010=4010;
    public static final int f011=4011;
    public static final int f012=4012;
    public static final int f5001=5001;
    public static final int f5002=5002;

}
