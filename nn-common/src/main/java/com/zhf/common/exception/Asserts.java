package com.zhf.common.exception;

import com.zhf.common.returnType.IErrorCode;

/**
 * 断言类，主要用来判断返回异常。
 * @Author 10276
 * @Date 2023/1/2 18:24
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    public static void failIsTrue(boolean flag,String message){
        if (flag){
            throw new ApiException(message);
        }
    }

}
