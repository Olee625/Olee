package com.olee.project.dto;

import lombok.Data;

@Data
public class PasswordReqDto {
    private String oldPassword;
    private String newPassword;
}
