package com.example.filemanager.model.exception;

public class CreateDirectoryException extends RuntimeException {
    public CreateDirectoryException(String directory) {
        super("Unable to create directory: " + directory);
    }
}
