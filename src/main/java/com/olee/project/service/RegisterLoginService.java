package com.olee.project.service;

import com.google.gson.JsonObject;
import com.olee.project.dto.RegisterRespDto;
import com.olee.project.dto.Response;
import com.olee.project.dto.UserRespDto;
import com.olee.project.model.User;

public interface RegisterLoginService {

    /**
     * @param user
     * @return com.example.spring.model.User
     * @description 注册新用户
     * @author Olee
     */
    Response<RegisterRespDto> registerUser(User user);

    /**
     * @param user
     * @return com.example.spring.model.User
     * @description 检查邮箱密码是否正确
     * @author Olee
     */
    Response<UserRespDto> checkUser(User user);

    Response<JsonObject> logout(String userId, String token);


}
