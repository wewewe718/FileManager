package com.example.filemanager.repository.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.filemanager.model.SortType;

public class SharedPreferencesSettingsRepository implements SettingsRepository {
    private static final String SORT_TYPE_SHARED_PREFERENCES_KEY = "SORT_TYPE_SHARED_PREFERENCES_KEY";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesSettingsRepository(@NonNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public SortType getSortType() {
        int sortType = sharedPreferences.getInt(SORT_TYPE_SHARED_PREFERENCES_KEY, 0);
        return SortType.fromInt(sortType);
    }

    @Override
    public void setSortType(@NonNull SortType sortType) {
        sharedPreferences
                .edit()
                .putInt(SORT_TYPE_SHARED_PREFERENCES_KEY, sortType.toInt())
                .apply();
    }
}
