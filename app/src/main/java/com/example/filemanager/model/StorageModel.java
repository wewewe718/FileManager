package com.example.filemanager.model;

import android.support.annotation.NonNull;

public class StorageModel {
    private StorageType type;
    private String name;
    private double totalSpace;
    private double usedSpace;

    public StorageModel(@NonNull StorageType type, @NonNull String name, double totalSpace, double usedSpace) {
        this.type = type;
        this.name = name;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
    }

    @NonNull
    public StorageType getType() {
        return type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public double getTotalSpace() {
        return totalSpace;
    }

    public double getUsedSpace() {
        return usedSpace;
    }
}
