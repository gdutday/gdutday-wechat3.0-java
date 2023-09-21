package com.elc.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 单良光
 * @Date: 2023/09/20/21:44
 * @Description:
 */
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public Result(T data) {
        this.code = 200;
        this.msg = "SUCCESS";
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
