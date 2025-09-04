package com.andres.notes.services;


import com.andres.notes.Jwt.JwtService;
import com.andres.notes.dto.*;
import com.andres.notes.exceptions.EntityAlreadyExistsException;
import com.andres.notes.exceptions.EntityNotFoundException;
import com.andres.notes.exceptions.InvalidPasswordException;
import com.andres.notes.exceptions.SamePasswordException;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolService rolService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public List<UserDto> getUsers(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserDto.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .id(user.getId())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("User with email " + email+ " not found"));
    }


    public void createUser(UserEntity user){
        if(userRepository.countByRoleName("admin")==0){
            user.setRole(rolService.findRoleByName("admin"));
        }else{
            if(userRepository.existsByEmail(user.getEmail()))
                throw  new EntityAlreadyExistsException("User already exist with email " + user.getEmail());
        }
        userRepository.save(user);
    }

    public AuthResponse login(LoginDto request){
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
    UserEntity user = (UserEntity) authentication.getPrincipal();
    String token = jwtService.getToken(user);
    return AuthResponse.builder()
                .token(token)
            .user(toDto(user))
                .build();
    }

    public boolean existsUserById(Long id){
        Optional<UserEntity> user = userRepository.findById(id);
        return user.isPresent();
    }


    public UserEntity findUserById(Long id){
        return userRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException("User with id " + id + " not found")
        );
    }

    public UserEntity findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()->new EntityNotFoundException("User with email " + email + " not found")
        );
    }
    public void deleteUser(Long id){
        userRepository.delete(findUserById(id));
    }

    public void updateUser(UserRequestDto user, Long id){
        UserEntity findUser = findUserById(id);

        if(user.getName()!=null){
            findUser.setName(user.getName());
        }
        if(user.getEmail()!=null){
            findUser.setEmail(user.getEmail());
        }
        if(user.getRole()!=null){
            findUser.setRole(rolService.findRoleById(user.getRole()));
        }
    }



    public void changePassword(ChangePasswordDto changePassword, String email){

        UserEntity findUser = findUserByEmail(email);

        if(!passwordEncoder.matches(changePassword.getCurrentPassword(), findUser.getPassword()))
            throw new InvalidPasswordException("Current password is incorrect");

        if(passwordEncoder.matches(changePassword.getNewPassword(),findUser.getPassword()))
            throw new SamePasswordException("New password must be different from the current one");
        findUser.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(findUser);
    }

    public UserDto toDto(UserEntity user){
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public UserEntity toEntity(UserDto user){
        return UserEntity.builder()
                .name(user.getName())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }


}
