package com.olee.project.advice;

import com.google.gson.JsonObject;
import com.olee.project.dto.Response;
import com.olee.project.enums.ResponseCode;
import com.olee.project.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class)
    public Response<JsonObject> serverErrorExceptionHandler(Exception e) {
        log.error("全局异常信息：", e);
        return new Response<>(ResponseCode.SERVER_ERROR, new JsonObject());
    }

    @ExceptionHandler(BizException.class)
    public Response<JsonObject> bizExceptionHandler(BizException e) {
        log.error("业务错误", e);

        return new Response<>(e.getResponseCode(), new JsonObject());
    }

}
