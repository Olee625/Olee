package com.olee.project.service;

import java.util.concurrent.TimeUnit;

public interface TokenManagementService {
    String generateToken(String email);

    String generateAndStoreToken(String userId, String email);

    boolean checkToken(String userId, String token);

    void deleteToken(String userId, String token);

    void deleteAllTokens(String userId);

    void setExpireOnKey(String userId, long timeout, TimeUnit unit);

}
