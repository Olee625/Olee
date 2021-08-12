package com.olee.project.exception;

import com.olee.project.enums.ResponseCode;

public class BizException extends RuntimeException {

    private ResponseCode responseCode;

    public BizException() {

    }

    public BizException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }


}
