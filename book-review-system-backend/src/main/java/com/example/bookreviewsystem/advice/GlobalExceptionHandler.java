package com.example.bookreviewsystem.advice;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一处理Spring validation 验证组件抛出的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK) // 统一http返回状态为200
    public SaResult methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        log.error("字段验证错误：", e);
        return SaResult.error("请填写所有必要的字段");
    }

    /**
     * 未登录的处理
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseStatus(HttpStatus.OK) // 统一http返回状态为200
    public SaResult notLoginExceptionHandler(NotLoginException e) {
        log.error("未登录：", e);
        return SaResult.get(400, "请登录", null);
    }

    /**
     * 处理其他异常
     *
     * @param e 未捕获的异常
     * @return 统一返回结构
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK) // 统一http返回状态为200
    public SaResult handlerException(Exception e) {
        log.error("服务器内部错误：", e);
        return SaResult.error("服务器内部错误，请联系管理员");
    }
}
