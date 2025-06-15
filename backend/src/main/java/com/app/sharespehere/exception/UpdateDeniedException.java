package com.app.sharespehere.exception;

public class UpdateDeniedException extends AccessDeniedException {
    public UpdateDeniedException() {
        super("Cannot update resource");
    }

    public UpdateDeniedException(String message) {
        super(message);
    }
}
