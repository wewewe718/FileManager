package com.example.filemanager.repository.settings;

import android.support.annotation.NonNull;

import com.example.filemanager.model.SortType;

public interface SettingsRepository {

    @NonNull
    SortType getSortType();

    void setSortType(@NonNull SortType sortType);
}
