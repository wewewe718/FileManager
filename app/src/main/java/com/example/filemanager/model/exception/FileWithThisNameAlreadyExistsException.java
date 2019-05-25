package com.example.filemanager.model.exception;

public class FileWithThisNameAlreadyExistsException extends RuntimeException {
    public FileWithThisNameAlreadyExistsException(String file) {
        super("File " + file + " already exists");
    }
}
