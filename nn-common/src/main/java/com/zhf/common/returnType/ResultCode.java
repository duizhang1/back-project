package com.zhf.common.returnType;

import lombok.AllArgsConstructor;

/**
 * @Author 10276
 * @Date 2023/1/2 18:32
 */
@AllArgsConstructor
public enum ResultCode implements IErrorCode{
    /**
     * 请求成功返回
     */
    SUCCESS("200","请求成功"),
    /**
     * 请求失败
     */
    FAILED("400","请求失败"),
    /**
     * 未登陆或者登陆过期
     */
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    /**
     * 没有权限
     */
    FORBIDDEN("403", "没有相关权限");

    private String code;
    private String message;



    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }
}
