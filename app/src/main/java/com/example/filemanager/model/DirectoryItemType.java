package com.example.filemanager.model;

public enum  DirectoryItemType {
    DIRECTORY,
    IMAGE,
    AUDIO,
    VIDEO,
    TEXT,
    OTHER;

    public int toInt() {
        return ordinal();
    }
}
