package com.andres.notes.controllers;

import com.andres.notes.dto.*;
import com.andres.notes.persistence.entity.RoleEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.services.RolService;
import com.andres.notes.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterDto user){

        UserEntity newUser  = UserEntity.builder()
                .name(user.getName())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .build();

        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .message("User created successful")
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto request){
        return ResponseEntity.ok(userService.login(request));
    }


    @PutMapping("/change-password")
    public  ResponseEntity<MessageResponse> changePassword(@RequestBody ChangePasswordDto changePassword, Principal principal){
        userService.changePassword(changePassword, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                MessageResponse.builder()
                        .message("Password successful updated")
                        .build()
        );
    }
}
