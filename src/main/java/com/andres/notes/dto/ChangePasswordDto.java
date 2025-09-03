package com.andres.notes.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChangePasswordDto {
    String currentPassword;
    String newPassword;
}
