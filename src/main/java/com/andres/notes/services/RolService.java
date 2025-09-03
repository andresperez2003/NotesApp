package com.andres.notes.services;


import com.andres.notes.exceptions.EntityAlreadyExistsException;
import com.andres.notes.persistence.entity.RoleEntity;
import com.andres.notes.persistence.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {


    private final RolRepository rolRepository;


    public List<RoleEntity> getRoles(){
        return rolRepository.findAll();
    }

    public RoleEntity findRoleById(Long id){
        return rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public RoleEntity findRoleByName(String name){
        return rolRepository.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public void createRol(RoleEntity rol){
        if(rolRepository.existsByName(rol.getName()))
            throw new EntityAlreadyExistsException("Rol " + rol.getName() + " already exists!");
        rolRepository.save(rol);
    }

    public void deleteRol(Long id){
        rolRepository.delete(findRoleById(id));
    }

    public void updateRol(RoleEntity role, Long id){
        RoleEntity findRole = findRoleById(id);

        if(role.getName() != null){
            findRole.setName(role.getName());
        }
        rolRepository.save(findRole);
    }

}
