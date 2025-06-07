package com.app.sharespehere.service;

import com.app.sharespehere.exception.CategoryNotFoundException;
import com.app.sharespehere.model.Category;
import com.app.sharespehere.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void saveCategory(String categoryName) {
        Category category = Category.builder()
                .name(categoryName)
                .build();
        this.saveCategory(category);
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public List<Category> getAllCategories() {
        List<Category> all = categoryRepository.findAll();
//        log.info("All categories {}",all);
        return all;
    }


}
