package com.olee.project.dto;

import com.olee.project.enums.ResponseCode;
import lombok.Data;


@Data
public class Response<T> {
    private Integer code;
    private String message;
    private T result;

    public Response(ResponseCode responseCode, T result) {
        this.code = responseCode.code();
        this.message = responseCode.message();
        this.result = result;
    }


    public static <T> Response<T> success(T result) {
        return new Response<>(ResponseCode.SUCCESS, result);

    }

    public static <T> Response<T> failure(ResponseCode responseCode, T result) {
        return new Response<>(responseCode, result);
    }
}
