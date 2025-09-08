package com.andres.notes.services;

import com.andres.notes.dto.CodeActivationDto;
import com.andres.notes.exceptions.EntityNotFoundException;
import com.andres.notes.persistence.entity.CodeActivationEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.repository.CodeActivationRepository;
import com.andres.notes.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CodeActivationService {

    private final UserRepository userRepository;
    private final CodeActivationRepository codeActivationRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.reset-token-expiration-minutes:15}")
    private int expirationMinutes;

    @Transactional
    public CodeActivationDto createCodeActivation(String email){
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isEmpty()) throw new EntityNotFoundException("User not found");

        Optional<CodeActivationEntity> findCodeActivation = codeActivationRepository.findByUser(user.get());
        findCodeActivation.ifPresent( codeActivationEntity -> codeActivationRepository.deleteByUser(user.get()));

        int codeInt = secureRandom.nextInt(1_000_000);
        String code = String.format("%06d", codeInt);

        CodeActivationEntity codeActivation = CodeActivationEntity.builder()
                .code(code)
                .user(user.get())
                .expiryDate(LocalDateTime.now())
                .build();

        codeActivationRepository.save(codeActivation);

        return CodeActivationDto.builder()
                .code(code)
                .email(user.get().getEmail())
                .expiryDate(LocalDateTime.now())
                .build();
    }


    @Transactional
    public boolean activeAccount (String code, String email){
        Optional<CodeActivationEntity> findCodeActivation = codeActivationRepository.findByCode(code);
        if(findCodeActivation.isEmpty()) return false;

        if(!email.equals(findCodeActivation.get().getUser().getEmail())) return false;

        UserEntity user = findCodeActivation.get().getUser();
        user.changeStatus();

        userRepository.save(user);
        return true;
    }

    public void deleteCode(UserEntity user){
        codeActivationRepository.deleteByUser(user);
        codeActivationRepository.flush();
    }
}
