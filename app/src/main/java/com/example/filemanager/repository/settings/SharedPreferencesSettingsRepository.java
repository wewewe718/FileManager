package com.example.filemanager.repository.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.filemanager.model.SortType;

public class SharedPreferencesSettingsRepository implements SettingsRepository {
    private static final String SORT_TYPE_SHARED_PREFERENCES_KEY = "SORT_TYPE_SHARED_PREFERENCES_KEY";

    private SharedPreferences sharedPreferences;
    private Listener listener;


    public SharedPreferencesSettingsRepository(@NonNull Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Override
    public void setListener(@NonNull Listener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener() {
        this.listener = null;
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

        if (listener != null) {
            listener.onSortTypeChanged(sortType);
        }
    }
}
