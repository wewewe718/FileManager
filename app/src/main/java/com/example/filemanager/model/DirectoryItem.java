package com.example.filemanager.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class DirectoryItem implements Serializable {
    private DirectoryItemType type;
    private String name;
    private String filePath;
    private Date lastModificationDate;
    private long fileSizeInBytes;

    public DirectoryItem(@NonNull DirectoryItemType type, @NonNull String name, @NonNull String filePath, @NonNull Date lastModificationDate, long fileSizeInBytes) {
        this.type = type;
        this.name = name;
        this.filePath = filePath;
        this.lastModificationDate = lastModificationDate;
        this.fileSizeInBytes = fileSizeInBytes;
    }

    @NonNull
    public DirectoryItemType getType() {
        return type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    @NonNull
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public long getFileSizeInBytes() {
        return fileSizeInBytes;
    }
}
