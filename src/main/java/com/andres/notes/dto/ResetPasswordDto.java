package com.andres.notes.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordDto {
    private String newPassword;
}
