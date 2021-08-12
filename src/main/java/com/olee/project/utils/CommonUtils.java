package com.olee.project.utils;

import com.olee.project.enums.ResponseCode;
import com.olee.project.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class CommonUtils {
    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    //生成userId，uuid返回的是16进制数，所以其实是不区分大小写的。当做字符串进行比较的时候才全部小写
    public static String generateUserId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String[] getUserIdAndToken(String xAuthorization) {
        String[] userIdAndToken = xAuthorization.split(" ");
        if (userIdAndToken.length != 2) {
            throw new BizException(ResponseCode.X_AUTHORIZATION_ERROR);
        }
        return userIdAndToken;
    }

}
