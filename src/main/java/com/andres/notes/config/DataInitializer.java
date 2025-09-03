package com.andres.notes.config;


import com.andres.notes.persistence.entity.RoleEntity;
import com.andres.notes.persistence.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RolRepository roleRepository) {
        return args -> {

            if (roleRepository.findByName("admin").isEmpty()) {
                RoleEntity role = new RoleEntity();
                role.setName("admin");
                roleRepository.save(role);
            }

            if (roleRepository.findByName("user").isEmpty()) {
                RoleEntity role = new RoleEntity();
                role.setName("user");
                roleRepository.save(role);
            }
        };
    }
}
