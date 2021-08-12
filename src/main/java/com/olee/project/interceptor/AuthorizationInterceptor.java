package com.olee.project.interceptor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.olee.project.annotation.AuthToken;
import com.olee.project.constant.Constant;
import com.olee.project.dto.AuthorizationThreadLocal;
import com.olee.project.dto.Response;
import com.olee.project.enums.ResponseCode;
import com.olee.project.service.TokenManagementService;
import com.olee.project.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenManagementService tokenManagementService;

    @Autowired
    public AuthorizationInterceptor(TokenManagementService tokenManagementService) {
        this.tokenManagementService = tokenManagementService;
    }


    //preHandle是请求执行前执行的
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(AuthToken.class) != null
                || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String xAuthorization = request.getHeader(Constant.HTTP_HEADER_AUTHORIZATION);
            //请求头有问题全局异常处理
            String acquiredUserId = CommonUtils.getUserIdAndToken(xAuthorization)[0];
            String acquiredToken = CommonUtils.getUserIdAndToken(xAuthorization)[1];
            Map<String, String> userIdAndToken = new HashMap<>();
            userIdAndToken.put("userId", acquiredUserId);
            userIdAndToken.put("token", acquiredToken);
            //把X-Authorization头的信息放入ThreadLocal
            AuthorizationThreadLocal.set(userIdAndToken);
            log.info("拿到的userId：{}，拿到的token值：{}", acquiredUserId, acquiredToken);
            //根据拿到的userid和token值校验token
            //token验证失败
            if (!(tokenManagementService.checkToken(acquiredUserId, acquiredToken))) {
                response.setCharacterEncoding("utf-8");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                Response<JsonObject> myResponse = Response.failure(ResponseCode.TOKEN_INVALIDATION, new JsonObject());
                Gson gson = new Gson();
                writer.write(gson.toJson(myResponse));
                return false;
            }

        }
        //token验证成功
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        AuthorizationThreadLocal.remove();
    }
}
