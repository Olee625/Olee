package com.olee.project.service;

import com.google.gson.JsonObject;
import com.olee.project.dto.RegisterRespDto;
import com.olee.project.dto.Response;
import com.olee.project.dto.UserRespDto;
import com.olee.project.enums.ResponseCode;
import com.olee.project.mapper.UserMapper;
import com.olee.project.model.User;
import com.olee.project.utils.CommonUtils;
import com.olee.project.utils.VerificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RegisterLoginServiceImpl implements RegisterLoginService {
    private final UserMapper userMapper;
    private final TokenManagementService tokenManagementService;
    private final int expireOnValueIntegerSeconds;
    private final int expireOnKeyIntegerSeconds;
    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    public RegisterLoginServiceImpl(UserMapper userMapper, TokenManagementService tokenManagementService,
                                    @Value("${redis.expireOnValueIntegerSeconds}") int expireOnValueIntegerSeconds,
                                    @Value("${redis.expireOnKeyIntegerSeconds}") int expireOnKeyIntegerSeconds) {
        this.userMapper = userMapper;
        this.tokenManagementService = tokenManagementService;
        this.expireOnValueIntegerSeconds = expireOnValueIntegerSeconds;
        this.expireOnKeyIntegerSeconds = expireOnKeyIntegerSeconds;
    }


    //用户注册
    @Override
    public Response<RegisterRespDto> registerUser(User user) {
        //参数校验
        VerificationUtils.verifyEmail(user.getEmail());
        VerificationUtils.verifyPassword(user.getPassword());
        //验证邮箱是否注册
        User existUser = userMapper.judgingEmailExists(user.getEmail());
        //如果该邮箱已经注册，返回错误码
        if (existUser != null) {
            return Response.failure(ResponseCode.EMAIL_ALREADY_EXISTS, new RegisterRespDto());
        }
        //将邮箱和密码进行注册,需要email、password、userId、createAt不为空
        //生成userId，uuid返回的是16进制数，所以其实是不区分大小写的。当做字符串进行比较的时候才全部小写
        //uuid规则:全局唯一标识符,是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的
        String userId = CommonUtils.generateUserId();
        //redis中对该userId生成zset并设置过期时间
        tokenManagementService.generateAndStoreToken(userId, user.getEmail());
        tokenManagementService.setExpireOnKey(userId, expireOnKeyIntegerSeconds, TimeUnit.SECONDS);
        user.setUserId(userId);
        //创建时间
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC);
        user.setCreateAt(time.format(sdf));
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //默认值在java中生成
        user.setNickname("user");
        user.setUpdateAt(time.format(sdf));
        userMapper.insert(user);
        RegisterRespDto registerResDto = new RegisterRespDto();
        registerResDto.setUserId(userId);
        registerResDto.setCreateAt(time.format(sdf));
        return Response.success(registerResDto);
    }

    //验证邮箱的密码正确
    @Override
    public Response<UserRespDto> checkUser(User user) {
        //参数校验 邮箱 密码合法性
        VerificationUtils.verifyEmail(user.getEmail());
        VerificationUtils.verifyPassword(user.getPassword());
        User findUser = userMapper.findByEmail(user.getEmail());
        if (findUser == null) {
            //邮箱不存在,返回错误码
            return Response.failure(ResponseCode.EMAIL_NOT_FOUND, new UserRespDto());
        }
        if (!findUser.getPassword().equals(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()))) {
            //密码错误
            return Response.failure(ResponseCode.PWD_NOT_CORRECT, new UserRespDto());
        }
        //密码正确，返回值
        String token = tokenManagementService.generateAndStoreToken(findUser.getUserId(), findUser.getEmail());
        //生成数据传输对象
        UserRespDto userDto = new UserRespDto(
                findUser.getUserId(),
                findUser.getEmail(),
                findUser.getNickname(),
                findUser.getAddress(),
                findUser.getCreateAt().toString(),
                findUser.getUpdateAt().toString(),
                token,
                expireOnValueIntegerSeconds);
        return Response.success(userDto);

    }

    @Override
    public Response<JsonObject> logout(String userId, String token) {
        //拦截器已经验证token成功
        //将该token值删除
        tokenManagementService.deleteToken(userId, token);
        return Response.success(new JsonObject());
    }


}
