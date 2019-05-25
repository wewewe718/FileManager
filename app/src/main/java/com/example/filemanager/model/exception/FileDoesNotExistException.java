package com.example.filemanager.model.exception;

public class FileDoesNotExistException extends RuntimeException {
    public FileDoesNotExistException(String file) {
        super("File " + file + " does not exist");
    }
}
