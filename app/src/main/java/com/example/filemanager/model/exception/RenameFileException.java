package com.example.filemanager.model.exception;

public class RenameFileException extends RuntimeException {
    public RenameFileException(String file) {
        super("Unable to rename file: " + file);
    }
}
