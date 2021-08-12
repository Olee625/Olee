package com.olee.project.config;


import com.olee.project.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    @Autowired
    public WebAppConfiguration(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    //拦截器配置
    public void addInterceptors(InterceptorRegistry registry) {

        //注册拦截器，创建拦截规则
        registry.addInterceptor(authorizationInterceptor).addPathPatterns(
                "/api/v1/user/register",
                "/api/v1/user/login",
                "/api/v1/user/getUserInfo",
                "/api/v1/user/logout",
                "/api/v1/user/updateUserInfo",
                "/api/v1/user/updatePassword");
    }
}
