package com.olee.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//注在类上，提供类的全参构造
public class UserRespDto {
    private String userId;
    private String email;
    private String nickname;
    private String address;
    private String createAt;
    private String updateAt;
    private String token;
    private Integer expiresIn;

    public UserRespDto(String userId, String email, String nickname, String address, String createAt, String updateAt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
