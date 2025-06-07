package com.app.sharespehere.exception;

public class DeleteDeniedException extends AccessDeniedException {
    public DeleteDeniedException() {
        super("Cannot delete resource");
    }
}
