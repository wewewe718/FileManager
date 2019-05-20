package com.example.filemanager.model;

import android.support.annotation.NonNull;

public class StorageModel {
    private String path;
    private long totalSpace;
    private long usedSpace;

    public StorageModel(@NonNull String path, long totalSpace, long usedSpace) {
        this.path = path;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getUsedSpace() {
        return usedSpace;
    }
}
