package com.olee.project.model;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class User {

    private Long id;
    private String userId;
    private String email;
    private String password;
    private String nickname;
    private String address;
    //todo 时间类型
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}