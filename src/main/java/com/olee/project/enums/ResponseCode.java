package com.olee.project.enums;

public enum ResponseCode {
    //枚举常量
    SUCCESS(0, "Success"),
    SERVER_ERROR(-10001, "Server internal error"),
    SERVER_BUSY(-10002, "Server is busy"),
    SERVER_TIMEOUT(-10003, "Server timeout"),
    EMAIL_MISTAKE(-20101, "Invalid email"),
    PWD_MISTAKE(-20102, "Invalid password"),
    EMAIL_ALREADY_EXISTS(-20111, "Email has been registered"),
    PWD_NOT_CORRECT(-20112, "The password is incorrect"),
    EMAIL_NOT_FOUND(-20113, "Email does not exist"),
    TOKEN_INVALIDATION(-20201, "User‘s token have expired"),
    TOKEN_MISTAKE(-20202, "User‘s token is incorrect"),
    NICKNAME_MISTAKE(-20103, "Invalid nickname"),
    ADDRESS_MISTAKE(-20104, "Invalid address"),
    OLD_PWD_MISTAKE(-20105, "The old password is invalid"),
    NEW_PWD_MISTAKE(-20106, "The new password is invalid"),
    NICKNAME_AND_ADDRESS_ARE_EMPTY(-20107, "The nickname and address are empty at the same time"),
    X_AUTHORIZATION_ERROR(-20108, "X-Authorization request header error"),
    OLD_PWD_NOT_CORRECT(-20112, "Old password is incorrect");
    private final Integer code;
    private final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
