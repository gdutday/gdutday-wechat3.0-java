package com.gdutelc.controller.wechat;

import com.gdutelc.framework.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author Ymri
 * @version 1.0
 * @since 2024/1/20 19:25
 * GdutDaysV2LoginController
 */
@Controller
@RequestMapping(value = "/gdutDay2/login")
public class GdutDaysV2LoginController {


    /**
     * 获取验证码接口
     *
     * @param request 请求
     * @return 验证码
     * @throws IOException IO异常
     */
    @GetMapping(value = "/sendVer.elc")
    @ResponseBody
    public AjaxResult sendVerification(HttpServletRequest request) throws IOException {
        return AjaxResult.success();
    }

    /**
     * 登录接口
     *
     * @param stuId 学号
     * @param pass 密码
     * @param vCode 验证码
     * @param jSessionId session
     * @return 登录结果
     */
    @PostMapping(value = "/login.elc")
    @ResponseBody
    public AjaxResult login(String stuId, String pass, String vCode, String jSessionId) {
        return AjaxResult.success();
    }

    /**
     * 获取用户反馈
     *
     * @param title 标题
     * @param content 内容
     * @param stuId 学号
     * @return 反馈结果
     */
    @PostMapping(value = "/getStuAdvice.elc")
    @ResponseBody
    public AjaxResult getStuAdvice(String title, String content, String stuId) {
        return AjaxResult.success();
    }

    /**
     * 按照页码和显示条目获取新闻
     *
     * @param pageCountStr 页码
     * @param limitCountStr 显示条目
     * @return 新闻
     */
    @GetMapping("/getLimitNews.elc")
    @ResponseBody
    public AjaxResult getLimitNews(String pageCountStr, String limitCountStr) {
        return AjaxResult.success();
    }

    /**
     * 获得新闻
     * @param keyword 关键字
     * @param pageCountStr 页码
     * @param limitCountStr 显示条目
     * @return 新闻
     */
    @PostMapping("/selectSomeNews.elc")
    @ResponseBody
    public AjaxResult selectSomeNews(String keyword, String pageCountStr, String limitCountStr) {
        return AjaxResult.success();
    }

    @GetMapping("/selectSpaceClazzrooms.elc")
    @ResponseBody
    public AjaxResult selectSpaceClazzrooms(String clazzroomId, String clazzroomName) {
        return AjaxResult.success();
    }

    /**
     * 获取公告
     *
     * @return 公告
     */
    @GetMapping("/announcement.elc")
    @ResponseBody
    public AjaxResult announcement() {
        return AjaxResult.success();
    }
}
