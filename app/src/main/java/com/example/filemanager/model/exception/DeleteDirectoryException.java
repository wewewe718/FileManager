package com.example.filemanager.model.exception;

public class DeleteDirectoryException extends RuntimeException {
    public DeleteDirectoryException(String directory) {
        super("Unable to delete directory: " + directory);
    }
}
