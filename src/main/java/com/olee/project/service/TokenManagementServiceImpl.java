package com.olee.project.service;

import com.olee.project.constant.Constant;
import com.olee.project.utils.ZsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenManagementServiceImpl implements TokenManagementService {
    @Value("${redis.expireOnValueIntegerSeconds}")
    private double expiresIntegerSeconds;
    private final ZsetUtil zsetUtil;

    @Autowired
    public TokenManagementServiceImpl(ZsetUtil zsetUtil) {
        this.zsetUtil = zsetUtil;
    }

    @Override
    public String generateToken(String email) {
        log.debug("进入generateToken函数了,邮箱为：{}", email);
        //生成token(格式为token:加密的邮箱-时间-六位随机数)
        //加密的邮箱
        return DigestUtils.md5DigestAsHex(email.getBytes()) + "-" +
                //时间
                //new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "-" +
                //六位随机字符串
                (new Random().nextInt(999999 - 111111 + 1) + 111111);
    }


    @Override
    //需要传入userid和email
    public String generateAndStoreToken(String userId, String email) {
        //根据email生成token值
        String token = generateToken(email);
        //生成创建时间的时间戳  score需要double类型
        Instant instant = Instant.now();
        //将token值，生成时间存放在对应的userId下
        String redisKey = Constant.REDIS_KEY_PREFIX + userId;
        zsetUtil.add(redisKey, token, instant.getEpochSecond());
        //移除其余token（最多5个）
        long num = zsetUtil.removeRange(redisKey, 0, -6);
        log.debug("generateAndStoreToken方法删除了{}个token", num);
        return token;
    }

    @Override
    //传入userid和token值
    public boolean checkToken(String userId, String token) {
        //根据userid和token查询reids库
        String redisKey = Constant.REDIS_KEY_PREFIX + userId;
        Double tokenCreateTimestamp = zsetUtil.score(redisKey, token);
        //判断token是否存在
        if (tokenCreateTimestamp == null || tokenCreateTimestamp == 0.0) {
            //不存在则验证失败
            return false;
        }
        //存在
        //验证失效时间
        long nowTime = Instant.now().getEpochSecond();
        return (tokenCreateTimestamp + expiresIntegerSeconds) >= nowTime;
    }

    @Override
    //删除一条token记录，而不是userId下的所有token
    public void deleteToken(String userId, String token) {
        String redisKey = Constant.REDIS_KEY_PREFIX + userId;
        long num = zsetUtil.remove(redisKey, token);
        log.info("deleteToken方法,userId:{},token:{},{}条token已经删除", userId, token, num);

    }

    @Override
    //用户修改密码后，删除所有该userId下的所有token
    public void deleteAllTokens(String userId) {
        String redisKey = Constant.REDIS_KEY_PREFIX + userId;
        boolean result = zsetUtil.removeKey(redisKey);
        log.info("用户:{}修改密码后，是否删除了key：{}", userId, result);
    }

    public void setExpireOnKey(String userId, long timeout, TimeUnit unit) {
        String redisKey = Constant.REDIS_KEY_PREFIX + userId;
        zsetUtil.setExpireOnKey(redisKey, timeout, unit);

    }
}
