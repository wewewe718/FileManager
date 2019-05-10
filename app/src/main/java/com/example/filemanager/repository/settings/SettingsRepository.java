package com.example.filemanager.repository.settings;

import android.support.annotation.NonNull;

import com.example.filemanager.model.SortType;

public interface SettingsRepository {

    interface Listener {
        void onSortTypeChanged(@NonNull SortType newSortType);
    }

    void setListener(@NonNull Listener listener);

    void removeListener();

    @NonNull
    SortType getSortType();

    void setSortType(@NonNull SortType sortType);
}
