package com.app.sharespehere.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String categoryName) {
        super("Category",categoryName);
    }
}
