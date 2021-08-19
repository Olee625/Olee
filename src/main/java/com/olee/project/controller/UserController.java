package com.olee.project.controller;

import com.google.gson.JsonObject;
import com.olee.project.annotation.AuthToken;
import com.olee.project.dto.*;
import com.olee.project.model.User;
import com.olee.project.service.InfoManagementService;
import com.olee.project.service.RegisterLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//由于主要的浏览器（例如Chrome）现在已符合规范并正确解释了UTF-8特殊字符 不需要charset = UTF-8参数。
@RequestMapping(value = "/api/v1/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final RegisterLoginService registerLoginService;
    private final InfoManagementService infoManagementService;

    @Autowired
    public UserController(RegisterLoginService registerLoginService, InfoManagementService infoManagementService) {
        this.registerLoginService = registerLoginService;
        this.infoManagementService = infoManagementService;
    }
    
    @PostMapping("/register")
    public Response<RegisterRespDto> register(@RequestBody User user) {
        //参数校验，邮箱和密码合法性，再对邮箱是否注册做判断，最后注册新用户，写入数据库
        return registerLoginService.registerUser(user);
    }

    @PostMapping("/login")
    public Response<UserRespDto> login(@RequestBody User user) {
        //密码正确，可以登录并返回数据
        return registerLoginService.checkUser(user);
    }

    @PostMapping("/logout")
    @AuthToken
    public Response<JsonObject> logout() {
        String userId = AuthorizationThreadLocal.get().get("userId");
        String token = AuthorizationThreadLocal.get().get("token");
        log.info("userId为{},进入到logout的controller层", userId);
        return registerLoginService.logout(userId, token);
    }

    @PostMapping("/getUserInfo")
    @AuthToken
    public Response<UserRespDto> getUserInfo() {
        //拿到userId
        String userId = AuthorizationThreadLocal.get().get("userId");
        log.info("userId为{},进入到getUserInfo的controller层", userId);
        return infoManagementService.getUserInfo(userId);

    }

    //更新用户信息
    @PostMapping("/updateUserInfo")
    @AuthToken
    public Response<JsonObject> updateUserInfo(@RequestBody User user) {
        //服务层更新操作
        String userId = AuthorizationThreadLocal.get().get("userId");
        log.info("userId为{},进入到updateUserInfo的controller层", userId);
        return infoManagementService.changeInfo(user, userId);

    }

    @AuthToken
    @PostMapping("/updatePassword")
    public Response<JsonObject> updatePassword(@RequestBody PasswordReqDto passwordDto) {
        //密码合法性校验和token校验通过后
        String userId = AuthorizationThreadLocal.get().get("userId");
        log.info("userId为{},进入到updatePassword的controller层", userId);
        return infoManagementService.changePassword(passwordDto, userId);

    }

}
