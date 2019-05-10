package com.example;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.filemanager.repository.directory.DirectoryRepository;
import com.example.filemanager.repository.directory.MockDirectoryRepository;
import com.example.filemanager.repository.settings.SettingsRepository;
import com.example.filemanager.repository.settings.SharedPreferencesSettingsRepository;
import com.example.filemanager.repository.storage.MockStorageListRepository;
import com.example.filemanager.repository.storage.StorageListRepository;

public class App extends Application {
    private StorageListRepository storageListRepository;
    private DirectoryRepository directoryRepository;
    private SettingsRepository settingsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        storageListRepository = new MockStorageListRepository();
        directoryRepository = new MockDirectoryRepository();
        settingsRepository = new SharedPreferencesSettingsRepository(this);
    }

    @NonNull
    public StorageListRepository getStorageListRepository() {
        return storageListRepository;
    }

    @NonNull
    public DirectoryRepository getDirectoryRepository() {
        return directoryRepository;
    }

    @NonNull
    public SettingsRepository getSettingsRepository() {
        return settingsRepository;
    }
}
