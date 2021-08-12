package com.olee.project.utils;

import com.olee.project.enums.ResponseCode;
import com.olee.project.exception.BizException;

public class VerificationUtils {
    private VerificationUtils() {
        throw new IllegalStateException("Utility class");
    }

    //邮箱格式规范
    public static void verifyEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new BizException(ResponseCode.EMAIL_MISTAKE);
        }
    }

    //密码格式规范
    public static void verifyPassword(String password) {
        if (password.length() > 20 || password.length() < 6) {
            throw new BizException(ResponseCode.PWD_MISTAKE);
        }
        char[] passwordList = password.toCharArray();
        for (char key : passwordList) {
            if (key < 33 || key > 126) {
                throw new BizException(ResponseCode.PWD_MISTAKE);
            }
        }
    }

    //用户昵称规范
    public static void verifyNickname(String nickname) {
        if (nickname != null) {
            if (nickname.length() > 32) {
                throw new BizException(ResponseCode.NICKNAME_MISTAKE);
            }
        }
    }

    /**
     * @param address
     */
    public static void verifyAddress(String address) {
        if (address != null) {
            if (address.length() > 255) {
                throw new BizException(ResponseCode.ADDRESS_MISTAKE);
            }
        }
    }
}
