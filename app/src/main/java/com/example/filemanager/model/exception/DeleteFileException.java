package com.example.filemanager.model.exception;

public class DeleteFileException extends RuntimeException {
    public DeleteFileException(String message) {
        super("Unable to delete file: " + message);
    }
}
