package com.olee.project;


import com.google.gson.Gson;
import com.olee.project.utils.ZsetUtil;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTests {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ZsetUtil zsetUtil;

    @Before
    public void setUp() {
        //此种方式可通过spring上下文来自动配置一个或多个controller
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUUID32() {
        //生成userId
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //System.out.println("uuid:"+uuid);

        //成token(格式为token:加密的邮箱-时间-六位随机数)
        StringBuilder token = new StringBuilder("token:");
        String email = "1037659019@qq.com";
        //加密的邮箱
        token.append(DigestUtils.md5DigestAsHex(email.getBytes()) + "-");
        //时间
        token.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "-");
        //六位随机字符串
        token.append(new Random().nextInt(999999 - 111111 + 1) + 111111);
        //System.out.println("token值："+token.toString());

        //密码加密：用户输入后，前端MD5加密后传输，到后端再次MD5加密后存入数据库
        String password1 = "ae1e4c53516bbb9c35f9c58268ac9f9d";
        String password2 = "kkkk1aaaa2llll3bbbb4";
        String md5_pwd1 = DigestUtils.md5DigestAsHex(password1.getBytes());
        String md5_pwd2 = DigestUtils.md5DigestAsHex(password2.getBytes());
        System.out.println(password1 + ":" + md5_pwd1 + ":" + md5_pwd1.length() + '\n' + password2 + ":" + md5_pwd2 + ":" + md5_pwd2.length());

        //uuid:d87b5ee833144d82af30fee2da5a4027
        //token值：token:8beb73609c07ef7122be0d4ddcd3130c-20210727152735458-378431

    }

    @Test
    public void timeTest() {
        /*
        // 当前时间:
        Calendar c = Calendar.getInstance();
        System.out.println(c);
        // 设置为北京时区:
        //c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        // 显示时间格式:
        var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        //设置时区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(sdf.format(c.getTime()).getClass().toString());
        System.out.println(sdf.format(c.getTime()).getClass().toString());
        */
        //当前时间
        DateTimeFormatter sdf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter sdf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //LocalDateTime localDateTime = LocalDateTime.parse(LocalDateTime.now(), sdf);
        Instant instant = Instant.now();
        String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(instant);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")).withZoneSameInstant(ZoneOffset.UTC);
        //LocalDateTime datetime = LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).format(sdf), sdf1);
        System.out.println(zonedDateTime);
        System.out.println(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        //ZonedDateTime createAt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+08:00")).withZoneSameInstant(ZoneOffset.UTC);
        //System.out.println("datetime:" + datetime);
        //1627983131
        //1627983163
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String createAt = LocalDateTime.now(ZoneOffset.UTC).format(sdf);
    }

    @Test
    public void registerTest() throws Exception {
        RequestBuilder request;
        /*
        标准请求：mysql数据库没有的邮箱
        "{\"email\":\"15862717625@qq.com\",\"password\":\"123456olee\"}"
        */
        //"createAt":"2021-08-05T11:34:32+0800"
        //
        String requestBody = "{\"email\":\"123@qq.com\",\"password\":\"123456olee\"}";
        request = post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        String responseString = mvc.perform(request)
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);


    }

    @Test
    public void loginTest() throws Exception {
        RequestBuilder request;
        String requestBody = "{\"email\":\"15862717625@qq.com\",\"password\":\"olee123\"}";
        request = post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);
        Gson gson = new Gson();
        log.info(gson.toJson(request));
    }

    @Test
    public void logoutTest() throws Exception {
        RequestBuilder request;
        String Authorization = "2105ae04227c415ba15c8a8af03428eb 04a73d6088ae02ffffc611632adf85ea-20210805170916735-581699";
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
        String Authorization = "2105ae04227c415ba15c8a8af03428eb 04a73d6088ae02ffffc611632adf85ea-20210805174045564-830266";
        String errorAuthorization = "123 45678";
        request = post("/api/v1/user/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Authorization", Authorization);

        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);

    }

    @Test
    public void updateUserInfoTest() throws Exception {
        RequestBuilder request;
        //考虑token是否过期，登录新建一个token
        String Authorization = "2105ae04227c415ba15c8a8af03428eb 04a73d6088ae02ffffc611632adf85ea-20210805174045564-830266";
        String errorAuthorization = "123 45678";
        String requestBody = "{\"nickname\":\"Jay\",\"address\":\"重庆市江北区\"}";
        String requestBody1 = "{\"address\":\"成都市锦江区\"}";
        String requestBody2 = "{\"nickname\":\"OleeDeng\"}";
        String requestBody3 = "{\"nickname\":\"OleeDeng😍\"}";
        String errorRequestBody1 = "{\"nickname\":\"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\",\"address\":\"重庆市江北区\"}";
        String errorRequestBody2 = "{\"nickname\":\"Olee\",\"address\":\"钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去请求钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去请求去去请求去去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱去钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱钱\"}";
        request = post("/api/v1/user/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody3)
                .header("X-Authorization", Authorization);

        String responseString = mvc.perform(request)
                .andReturn().getResponse().getContentAsString();
        log.info("response:" + responseString);

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

        //Gson配上之前：Response(code=-20201, message=User‘s token have expired, result={})
        //Gson配置之后：{"code":-20201,"message":"User‘s token have expired","result":{}}

    }

    @Test
    public void redisTest() throws Exception {
        // 保存字符串
        //stringRedisTemplate.opsForValue().set("1816", "15862717625");
        //Assert.assertEquals("123", stringRedisTemplate.opsForValue().get("Olee"));
        //tokenManagementService.storeToken("Olee","1234@qq.com");
        Double i = zsetUtil.score("Olee", "token:88d5cb704d88bdad67d000eee4782000-20210803184259431-595383");
        System.out.println(i);


    }

}
