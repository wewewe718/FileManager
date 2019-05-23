package com.example.filemanager.util.filesystem;

import android.support.annotation.NonNull;

import java.io.File;

public class DirectoryContentUtil {
    @NonNull
    public static File[] getDirectoryContent(@NonNull String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        return (files != null) ? files : new File[0];
    }
}
