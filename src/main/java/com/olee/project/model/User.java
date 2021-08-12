package com.olee.project.model;

import lombok.Data;


@Data
public class User {

    private Long id;
    private String userId;
    private String email;
    private String password;
    private String nickname;
    private String address;
    //todo 时间类型
    private String createAt;
    private String updateAt;


}