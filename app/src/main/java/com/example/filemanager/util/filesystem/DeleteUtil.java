package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import com.example.filemanager.model.exception.DeleteDirectoryException;
import com.example.filemanager.model.exception.DeleteFileException;
import com.example.filemanager.model.exception.FileDoesNotExistException;

import java.io.File;

public class DeleteUtil {

    public static void delete(@NonNull File fileOrDirectory) {
        if (!fileOrDirectory.exists()) {
            throw new FileDoesNotExistException();
        }

        if (!fileOrDirectory.isDirectory()) {
            deleteFile(fileOrDirectory);
        } else {
            deleteDirectory(fileOrDirectory);
        }
    }

    private static void deleteFile(@NonNull File file) {
        boolean isFileDeleted = file.delete();
        if (!isFileDeleted) {
            throw new DeleteFileException();
        }
    }

    private static void deleteDirectory(@NonNull File directory) {
        deleteDirectoryContent(directory);
        deleteEmptyDirectory(directory);
    }

    private static void deleteDirectoryContent(@NonNull File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File fileOrDirectory : files) {
            delete(fileOrDirectory);
        }
    }

    private static void deleteEmptyDirectory(@NonNull File directory) {
        boolean isDirectoryDeleted = directory.delete();
        if (!isDirectoryDeleted) {
            throw new DeleteDirectoryException();
        }
    }
}
