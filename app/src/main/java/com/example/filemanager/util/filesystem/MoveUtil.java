package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import java.io.File;

public class MoveUtil {

    public static void move(@NonNull String sourceFileOrDirectory, @NonNull String targetDirectoryPath) {
        CopyUtil.copy(sourceFileOrDirectory, targetDirectoryPath);
        DeleteUtil.delete(new File(sourceFileOrDirectory));
    }

}
