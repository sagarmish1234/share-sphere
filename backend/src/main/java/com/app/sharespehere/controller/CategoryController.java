package com.app.sharespehere.controller;

import com.app.sharespehere.model.Category;
import com.app.sharespehere.service.CategoryService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @PostMapping("/categories")
    public void createCategory(@RequestParam String name) {
        categoryService.saveCategory(name);
    }

    @GetMapping("/categories/{name}")
    public Category getCategory(@PathVariable String name){
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/categories")
    public List<Category> allCategories() {
        return categoryService.getAllCategories();
    }



}
