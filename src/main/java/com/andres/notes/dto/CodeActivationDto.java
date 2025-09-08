package com.andres.notes.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeActivationDto {

    String email;
    String code;
    LocalDateTime expiryDate;
}
