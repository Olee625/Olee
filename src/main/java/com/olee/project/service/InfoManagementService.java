package com.olee.project.service;

import com.google.gson.JsonObject;
import com.olee.project.dto.PasswordReqDto;
import com.olee.project.dto.Response;
import com.olee.project.dto.UserRespDto;
import com.olee.project.model.User;

public interface InfoManagementService {
    Response<UserRespDto> getUserInfo(String userId);

    Response<JsonObject> changeInfo(User user, String userId);

    Response<JsonObject> changePassword(PasswordReqDto passwordReqDto, String userId);
}
