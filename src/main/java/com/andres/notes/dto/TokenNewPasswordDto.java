package com.andres.notes.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenNewPasswordDto {

    private String email;
    private String token;
    private LocalDateTime expiryDate;
}
