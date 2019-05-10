package com.example.filemanager.model;

import android.support.annotation.NonNull;

public enum SortType {
    NAME,
    DATE,
    TYPE,
    SIZE;

    public int toInt() {
        return ordinal();
    }

    @NonNull
    public static SortType fromInt(int value) {
        return values()[value];
    }
}
