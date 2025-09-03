package com.andres.notes.services;

import com.andres.notes.dto.CategoryRequestDto;
import com.andres.notes.exceptions.EntityAlreadyExistsException;
import com.andres.notes.exceptions.EntityNotFoundException;
import com.andres.notes.exceptions.UserMismatchException;
import com.andres.notes.persistence.entity.CategoryEntity;
import com.andres.notes.persistence.entity.UserEntity;
import com.andres.notes.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public List<CategoryEntity> getCategories(){
        return categoryRepository.findAll();
    }

    public List<CategoryEntity> getCategoriesByUser(String email){
        return categoryRepository.findByUserEmail(email);
    }

    public CategoryEntity findCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Category with id " + id+ " not found")
        );
    }

    public void createCategory(CategoryRequestDto category, String email){
        UserEntity user = userService.findUserByEmail(email);
        CategoryEntity newCategory = CategoryEntity.builder()
                .name(category.getName())
                .user(user)
                .build();
        if(categoryRepository.existsByName(category.getName())){
            throw new EntityAlreadyExistsException("Category with name " + category.getName() + " already exists");
        }

        categoryRepository.save(newCategory);
    }



    public void deleteCategory(Long id, String email){
        UserEntity user = userService.getUserByEmail(email);
        CategoryEntity findCategory = findCategoryById(id);
        if(!user.getId().equals(findCategory.getUser().getId())){
            throw new UserMismatchException("The provided user does not match the authenticated user");
        }
        categoryRepository.delete(findCategoryById(id));
    }

    public void updateCategory(CategoryEntity category,Long id, String email){
        UserEntity user = userService.getUserByEmail(email);

        CategoryEntity findCategory = findCategoryById(id);
        if(!user.getId().equals(findCategory.getUser().getId())){
            throw new UserMismatchException("The provided user does not match the authenticated user");
        }
        if(category.getName()!= null){
            findCategory.setName(category.getName());
        }
        categoryRepository.save(findCategory);
    }
}
