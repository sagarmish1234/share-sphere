package com.app.sharespehere.exception;

public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String resourceName) {
        super("Resource", resourceName);
    }
}
