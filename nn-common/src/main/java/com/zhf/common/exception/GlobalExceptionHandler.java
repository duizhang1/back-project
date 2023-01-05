package com.zhf.common.exception;

import com.zhf.common.returnType.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 10276
 * @Date 2023/1/2 19:23
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义抛出的异常
     * @param e 异常
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handlerApiException(ApiException e){
        LOGGER.error("error: ",e);
        if (e.getErrorCode() != null){
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult handlerException(Exception e){
        LOGGER.error("error: ",e);
        return CommonResult.failed("出现异常，请联系管理员");
    }
}
