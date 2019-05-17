package com.example.filemanager.util;

public class Unit {
    private static final Unit INSTANCE = new Unit();

    public static Unit get() {
        return INSTANCE;
    }
}
