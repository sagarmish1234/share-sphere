package com.app.sharespehere.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, String resourceName ) {
        super(String.format("%s %s not found",resource,resourceName));
    }
}
