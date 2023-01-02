package com.zhf.common.returnType;

/**
 * @Author 10276
 * @Date 2023/1/2 18:29
 */
public interface IErrorCode {

    /**
     * 获得当前请求的返回码
     * @return
     */
    String getCode();

    /**
     * 获得当前请求的返回信息
     * @return
     */
    String getMsg();

}
