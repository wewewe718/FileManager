package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import com.example.filemanager.model.exception.CreateDirectoryException;
import com.example.filemanager.model.exception.DirectoryWithThisNameAlreadyExistsException;

import java.io.File;

public class CreateDirectoryUtil {

    public static void createDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        File directory = new File(rootDirectoryFullPath, newDirectoryName);
        if (directory.exists()) {
            throw new DirectoryWithThisNameAlreadyExistsException();
        }

        boolean isDirectoryCreated = directory.mkdir();
        if (!isDirectoryCreated) {
            throw new CreateDirectoryException();
        }
    }
}
