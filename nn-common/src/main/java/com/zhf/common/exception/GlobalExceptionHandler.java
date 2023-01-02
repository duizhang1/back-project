package com.zhf.common.exception;

import com.zhf.common.returnType.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author 10276
 * @Date 2023/1/2 19:23
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义抛出的异常
     * @param e 异常
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    public CommonResult handlerApiException(ApiException e){
        if (e.getErrorCode() != null){
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public CommonResult handlerBindException(BindException e){
        return CommonResult.failed("出现异常，请联系管理员");
    }
}
