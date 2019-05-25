package com.example.filemanager.model.exception;

public class CreateFileException extends RuntimeException {

    public CreateFileException() {
    }

    public CreateFileException(String file) {
        super("Unable to create file: " + file);
    }

    public CreateFileException(Throwable cause) {
        super(cause);
    }

    public CreateFileException(String file, Throwable cause) {
        super("Unable to create file: " + file, cause);
    }
}
