package com.example.filemanager.model;

import android.support.annotation.NonNull;

public class StorageModel {
    private StorageType type;
    private String name;
    private long totalSpace;
    private long freeSpace;

    public StorageModel(@NonNull StorageType type, @NonNull String name, long totalSpace, long freeSpace) {
        this.type = type;
        this.name = name;
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
    }

    @NonNull
    public StorageType getType() {
        return type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getFreeSpace() {
        return freeSpace;
    }
}
