package com.olee.project.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Configuration
public class GsonConfig {
    @Bean
    public HttpMessageConverter customConverters() {
        // 创建 convert 消息转换对象
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        // 通过构建器设置Gson的相关设置,生成Gson对象
        Gson gson = new GsonBuilder()
                .create();
        //将设置好的Gson对象，传递给消息转化器
        gsonHttpMessageConverter.setGson(gson);
        return gsonHttpMessageConverter;
    }
}
