package com.example.filemanager.model.exception;

public class DirectoryWithThisNameAlreadyExistsException extends RuntimeException {
    public DirectoryWithThisNameAlreadyExistsException(String directory) {
        super("Directory " + directory + " already exists");
    }
}
