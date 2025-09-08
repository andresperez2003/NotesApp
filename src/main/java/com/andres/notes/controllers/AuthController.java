package com.andres.notes.controllers;

import com.andres.notes.dto.*;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {



    private final UserService userService;
    private final ResetPasswordService resetPasswordService;
    private final CodeActivationService codeActivationService;
    private final MailClientService mailClientService;


    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterDto user){

        UserEntity newUser  = UserEntity.builder()
                .name(user.getName())
                .password(user.getPassword())
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


    @PostMapping("reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@RequestParam String email){
        TokenNewPasswordDto token = resetPasswordService.createPasswordReset(email);
        if(token == null ) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(MessageResponse.builder().message("Token successful created").build());
    }


    @PostMapping("confirm-reset-password")
    public ResponseEntity<MessageResponse> confirmResetPassword(@RequestParam String token, @RequestBody ResetPasswordDto request){
        boolean success = resetPasswordService.resetPassword(
                token,
                request.getNewPassword()
        );

        if(!success) return ResponseEntity.badRequest().body(MessageResponse.builder().message("Incorrect or expired code").build());

        return ResponseEntity.ok().body(MessageResponse.builder().message("Password successful updated").build());
    }


    @PostMapping("activate-account")
    public ResponseEntity<MessageResponse> useCodeActivation(@RequestParam String email, @RequestBody UseCodeActivationDto codeActivation){
        boolean code = codeActivationService.activeAccount(codeActivation.getCode(), email);
        if(!code) return ResponseEntity.ok().body(MessageResponse.builder().message("Cant activate user").build());

        return ResponseEntity.ok().body(MessageResponse.builder().message("User activated").build());
    }



}
