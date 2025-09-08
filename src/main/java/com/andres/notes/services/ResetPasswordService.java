package com.andres.notes.services;


import com.andres.notes.dto.TokenNewPasswordDto;
import com.andres.notes.persistence.entity.ResetPasswordEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.repository.ResetPasswordRepository;
import com.andres.notes.persistence.repository.UserRepository;
import com.andres.notes.util.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final UserRepository userRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailClientService mailClientService;

    @Value("${app.frontend-change-password-url}")
    private String resetPasswordUrl;


    @Value("${app.reset-token-expiration-minutes:15}")
    private int expirationMinutes;

    @Transactional
    public TokenNewPasswordDto createPasswordReset(String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isEmpty()) return null;

        Optional<ResetPasswordEntity> reset = resetPasswordRepository.findByUser(user.get());
        reset.ifPresent(resetPasswordEntity -> deleteByUser(user.get()));

        String token = UUID.randomUUID().toString();


        ResetPasswordEntity resetPassword = ResetPasswordEntity.builder()
                .token(token)
                .user(user.get())
                .expiryDate(LocalDateTime.now().plusMinutes(expirationMinutes))
                .build();



        resetPasswordRepository.save(resetPassword);
        resetPasswordRepository.flush();

        mailClientService.sendMail(user.get().getEmail(),"Cambio de contraseña en NoteApp ",
                MailClientService.buildResetPasswordTemplate(user.get().getName(), resetPasswordUrl+resetPassword.getToken() )
                );

        return TokenNewPasswordDto.builder()
                .token(resetPassword.getToken())
                .expiryDate(resetPassword.getExpiryDate())
                .email(resetPassword.getUser().getEmail())
                .build();
    }


    @Transactional
    public boolean resetPassword(String token, String newPassword ){
        Optional<ResetPasswordEntity> resetPassword = resetPasswordRepository.findByToken(token);

        if(resetPassword.isEmpty()) return false;

        ResetPasswordEntity getResetPassword = resetPassword.get();
        if(getResetPassword.getExpiryDate().isBefore(LocalDateTime.now())) return false;

        if(!PasswordValidator.isValid(newPassword)){
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }

        UserEntity user = getResetPassword.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        deleteByUser(user);

        return true;
    }

    public void deleteByUser(UserEntity user){
        resetPasswordRepository.deleteByUser(user);
        resetPasswordRepository.flush();
    }


}
