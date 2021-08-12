package com.olee.project.service;

import com.google.gson.JsonObject;
import com.olee.project.dto.PasswordReqDto;
import com.olee.project.dto.Response;
import com.olee.project.dto.UserRespDto;
import com.olee.project.enums.ResponseCode;
import com.olee.project.exception.BizException;
import com.olee.project.mapper.UserMapper;
import com.olee.project.model.User;
import com.olee.project.utils.VerificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class InfoManagementServiceImpl implements InfoManagementService {
    private final UserMapper userMapper;
    private final TokenManagementService tokenManagementService;
    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    public InfoManagementServiceImpl(UserMapper userMapper, TokenManagementService tokenManagementService) {
        this.userMapper = userMapper;
        this.tokenManagementService = tokenManagementService;
    }


    //查看用户信息 注：userId是已经token校验后的，所以userId一定存在
    @Override
    public Response<UserRespDto> getUserInfo(String userID) {
        User user = userMapper.findByUserId(userID);
        UserRespDto userDto = new UserRespDto(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getAddress(),
                user.getCreateAt().format(sdf),
                user.getUpdateAt().format(sdf));
        return Response.success(userDto);
    }

    //更新用户信息,userId是已经token校验后的，所以userId一定存在
    @Override
    public Response<JsonObject> changeInfo(User requestUser, String userId) {
        //校验nickname和address参数合法性
        log.info("controller层接收到的nickname：" + requestUser.getNickname() + ",address：" + requestUser.getAddress());
        VerificationUtils.verifyNickname(requestUser.getNickname());
        VerificationUtils.verifyAddress(requestUser.getAddress());
        if ((requestUser.getNickname() == null || "".equals(requestUser.getNickname())) &&
                (requestUser.getAddress() == null || "".equals(requestUser.getAddress()))) {
            throw new BizException(ResponseCode.NICKNAME_AND_ADDRESS_ARE_EMPTY);
        }
        User user = new User();
        user.setUserId(userId);
        user.setNickname(requestUser.getNickname());
        user.setAddress(requestUser.getAddress());
        //更新时间
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
        user.setUpdateAt(time);
        userMapper.updateNicknameAndAddress(user);
        return Response.success(new JsonObject());
    }

    //修改密码,userId是已经token校验后的，所以userId一定存在
    @Override
    public Response<JsonObject> changePassword(PasswordReqDto passwordReqDto, String userId) {
        //校验密码合法性
        VerificationUtils.verifyPassword(passwordReqDto.getOldPassword());
        VerificationUtils.verifyPassword(passwordReqDto.getNewPassword());
        if (passwordReqDto.getOldPassword().equals(passwordReqDto.getNewPassword())) {
            return Response.failure(ResponseCode.NEW_PWD_MISTAKE, new JsonObject());
        }

        //mysql查询到该用户
        User user = userMapper.checkPassword(userId);
        //拿到用户的md5加密的老密码
        String userOldPassword = user.getPassword();
        String userNewPassword = DigestUtils.md5DigestAsHex(passwordReqDto.getOldPassword().getBytes());
        //比较用户的老密码和得到的老密码是否相同
        if (!(userOldPassword.equals(userNewPassword))) {
            //不相同返回老密码错误
            return Response.failure(ResponseCode.OLD_PWD_NOT_CORRECT, new JsonObject());
        }
        //相同则修改密码为新密码
        user.setPassword(userNewPassword);
        //更新更新时间
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
        user.setUpdateAt(time);
        userMapper.updatePassword(user);
        tokenManagementService.deleteAllTokens(userId);
        return Response.success(new JsonObject());
    }
}
