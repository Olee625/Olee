package com.olee.project.controller;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserControllerTest {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    Gson gson = new Gson();


    @Before
    public void setUp() {
        //此种方式可通过spring上下文来自动配置一个或多个controller
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void registerTest() throws Exception {
        RequestBuilder request;
        String requestBody = "{\"email\":\"1037659019@qq.com\",\"password\":\"123456olee\"}";
        String requestBody1 = "{\"email\":\"15862717625@qq.com\",\"password\":\"123456olee\"}";
        request = post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        String responseString = mvc.perform(request)
                //.andDo(print())
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);


    }

    @Test
    public void loginTest() throws Exception {
        RequestBuilder request;
        String requestBody = "{\"email\":\"1037659019@qq.com\",\"password\":\"123456olee\"}";
        String requestBody1 = "{\"email\":\"15862717625@qq.com\",\"password\":\"123456olee\"}";
        request = post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);
    }

    @Test
    public void logoutTest() throws Exception {
        RequestBuilder request;
        String Authorization = "58c61751517049aa8782da474c290cd1 8beb73609c07ef7122be0d4ddcd3130c-20210812111032179-721302";
        //错误参数 "123 45678"
        String errorAuthorization = "123 45678";
        request = post("/api/v1/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Authorization", Authorization);
        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);

    }

    @Test
    public void getUserInfoTest() throws Exception {
        RequestBuilder request;
        //考虑token是否过期，登录新建一个token
        String Authorization = "7e445a77470a49d899a7f61d336681ec 8beb73609c07ef7122be0d4ddcd3130c-20210812143718092-859653";
        String errorAuthorization = "123 45678";
        String errorAuthorization1 = "cc8dd5a058404a8f92aeeb88561f52578beb73609c07ef7122be0d4ddcd3130c-20210811092512748-260818";
        request = post("/api/v1/user/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Authorization", Authorization);

        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);
        String expectedResponse = "{\"code\":0,\"message\":\"Success\",\"result\":{\"userId\":\"58c61751517049aa8782da474c290cd1\",\"email\":\"1037659019@qq.com\",\"nickname\":\"user\",\"createAt\":\"2021-08-12T03:08:05Z\",\"updateAt\":\"2021-08-12T03:08:05Z\"}}";
        Assert.assertThat(responseString, is(expectedResponse));

    }

    @Test
    public void updateUserInfoTest() throws Exception {

        String Authorization = "7e445a77470a49d899a7f61d336681ec 8beb73609c07ef7122be0d4ddcd3130c-20210812143718092-859653";
        String requestBody = "{\"nickname\":\"Olee\",\"address\":\"重庆市江北区\"}";
        String requestBody1 = "{\"address\":\"成都市锦江区\"}";
        String requestBody2 = "{\"nickname\":\"Olee\"}";
        String requestBody3 = "{\"nickname\":\"OleeDeng😍\"}";
        String requestBody4 = "{}";

        //成功______________________________________________________________________________________________
        RequestBuilder request1 = post("/api/v1/user/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1)
                .header("X-Authorization", Authorization);
        String responseString1 = mvc.perform(request1)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString1);
        String expectedResponse1 = "{\"code\":0,\"message\":\"Success\",\"result\":{}}";
        Assert.assertThat(responseString1, is(expectedResponse1));
/*
        //-20201：用户凭证已失效（过期、登出）-20201：用户凭证已失效（过期、登出）_______________________________________
        String errorAuthorization = "123 45678";
        RequestBuilder request2;
        request2 = post("/api/v1/user/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("X-Authorization", errorAuthorization);
        String responseString2 = mvc.perform(request2)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString2);
        String expectedResponse2 = "{\"code\":-20201,\"message\":\"User‘s token have expired\",\"result\":{}}";
        Assert.assertThat(responseString2, is(expectedResponse2));

        //-20103：昵称不合法____________________________________________________________________________________
        String errorRequestBody1 = "{\"nickname\":\"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\",\"address\":\"重庆市江北区\"}";
        RequestBuilder request3;
        request3 = post("/api/v1/user/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(errorRequestBody1)
                .header("X-Authorization", Authorization);
        String responseString3 = mvc.perform(request3)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString3);
        String expectedResponse3 = "{\"code\":-20103,\"message\":\"Invalid nickname\",\"result\":{}}";
        Assert.assertThat(responseString3, is(expectedResponse3));

        //-20103：地址不合法____________________________________________________________________________________
        String errorRequestBody2 = "{\"nickname\":\"Olee\",\"address\":\"钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去请求钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去请求去去请求去去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱\"}";
        RequestBuilder request4;
        request4 = post("/api/v1/user/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(errorRequestBody2)
                .header("X-Authorization", Authorization);
        String responseString4 = mvc.perform(request4)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString4);
        String expectedResponse4 = "{\"code\":-20104,\"message\":\"Invalid address\",\"result\":{}}";
        Assert.assertThat(responseString4, is(expectedResponse4));

*/
    }

    @Test
    public void updatePasswordTest() throws Exception {
        RequestBuilder request;
        //考虑token是否过期，登录新建一个token
        String Authorization = "2105ae04227c415ba15c8a8af03428eb 04a73d6088ae02ffffc611632adf85ea-20210805155607410-898667";
        //token验证失败
        String errorAuthorization = "123 45678";
        String requestBody = "{\"oldPassword\":\"olee123456789\",\"newPassword\":\"olee123\"}";
        //老密码不合法
        String errorRequestBody1 = "{\"oldPassword\":\"重庆市江北区\",\"newPassword\":\"123456789\"}";
        //新密码不合法
        String errorRequestBody2 = "{\"oldPassword\":\"123456789\",\"newPassword\":\"重庆市江北区\"}";
        //老密码不正确
        String errorRequestBody3 = "{\"oldPassword\":\"123456789\",\"newPassword\":\"olee123456789\"}";
        request = post("/api/v1/user/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("X-Authorization", Authorization);

        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);
        String expectedResponse = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"Success\",\n" +
                "  \"result\": {\n" +
                "    \"userId\": \"2105ae04227c415ba15c8a8af03428eb\",\n" +
                "    \"email\": \"15862717625@qq.com\",\n" +
                "    \"nickname\": \"Jay\",\n" +
                "    \"address\": \"重庆市江北区\",\n" +
                "    \"createAt\": \"2021-08-04T14:49:23+0800\",\n" +
                "    \"updateAt\": \"2021-08-04T14:49:24+0800\"\n" +
                "  }\n" +
                "}";
        Assert.assertThat(responseString, is(expectedResponse));


    }


}
