package com.zh.shenshouexpensetracker.handle;


import com.zh.shenshouexpensetracker.exception.TokenOverDueException;
import com.zh.shenshouexpensetracker.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenOverDueException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleTokenOverDueException(TokenOverDueException e) {
        log.warn("Token过期: {}", e.getMessage());  // 补充日志
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result allException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);

        // 生产环境返回模糊信息，开发环境可返回详细点
        return Result.error(e.getMessage());
    }
}