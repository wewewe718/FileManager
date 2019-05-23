package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import com.example.filemanager.model.exception.DirectoryWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.FileDoesNotExistException;
import com.example.filemanager.model.exception.FileWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.RenameFileException;

import java.io.File;

public class RenameUtil {

    public static void rename(@NonNull String oldPath, @NonNull String newName) {
        File oldFile = new File(oldPath);
        if (!oldFile.exists()) {
            throw new FileDoesNotExistException();
        }

        File newFile = new File(oldFile.getParent(), newName);
        if (newFile.exists()) {
            if (newFile.isDirectory()) {
                throw new DirectoryWithThisNameAlreadyExistsException();
            } else {
                throw new FileWithThisNameAlreadyExistsException();
            }
        }

        boolean isFileRenamed = oldFile.renameTo(newFile);
        if (!isFileRenamed) {
            throw new RenameFileException();
        }
    }
}
