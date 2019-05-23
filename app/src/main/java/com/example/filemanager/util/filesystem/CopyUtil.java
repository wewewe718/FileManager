package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import com.example.filemanager.model.exception.CopyFileException;
import com.example.filemanager.model.exception.CreateDirectoryException;
import com.example.filemanager.model.exception.CreateFileException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class CopyUtil {

    public static void copy(@NonNull String sourceFileOrDirectory, @NonNull String targetDirectoryPath) {
        File src = new File(sourceFileOrDirectory);
        File dst = new File(targetDirectoryPath, src.getName());
        if (src.isDirectory()) {
            copyDirectory(src, dst);
        } else {
            copyFile(src, dst);
        }
    }

    private static void copyDirectory(@NonNull File sourceDirectory, @NonNull File targetDirectory) {
        String files[] = sourceDirectory.list();
        for (String file : files) {
            String src = (new File(sourceDirectory, file).getPath());
            String dst = targetDirectory.getPath();
            copy(src, dst);
        }
    }

    private static void copyFile(@NonNull File sourceFile, @NonNull File destFile) {
        if (!destFile.getParentFile().exists()) {
            boolean isDirectoryCreated = destFile.getParentFile().mkdirs();
            if (!isDirectoryCreated) {
                throw new CreateDirectoryException();
            }
        }

        if (!destFile.exists()) {
            createFile(destFile);
        }

        try (
                FileChannel source = new FileInputStream(sourceFile).getChannel();
                FileChannel destination = new FileOutputStream(destFile).getChannel()
        ) {
            destination.transferFrom(source, 0, source.size());
        } catch (Exception ex) {
            throw new CopyFileException(ex);
        }
    }

    private static void createFile(@NonNull File file) {
        try {
            boolean isFileCreated = file.createNewFile();
            if (!isFileCreated) {
                throw new CreateFileException();
            }
        } catch (Exception ex) {
            throw new CreateFileException(ex);
        }
    }
}
