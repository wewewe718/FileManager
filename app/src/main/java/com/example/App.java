package com.example;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.filemanager.repository.directory.DirectoryRepository;
import com.example.filemanager.repository.directory.FileSystemDirectoryRepository;
import com.example.filemanager.repository.directory.MockDirectoryRepository;
import com.example.filemanager.repository.settings.SettingsRepository;
import com.example.filemanager.repository.settings.SharedPreferencesSettingsRepository;
import com.example.filemanager.repository.storage.FileSystemStorageRepository;
import com.example.filemanager.repository.storage.MockStorageRepository;
import com.example.filemanager.repository.storage.StorageRepository;

public class App extends Application {
    private StorageRepository storageRepository;
    private DirectoryRepository directoryRepository;
    private SettingsRepository settingsRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        //storageRepository = new MockStorageRepository();
        storageRepository = new FileSystemStorageRepository(this);

        //directoryRepository = new MockDirectoryRepository();
        directoryRepository = new FileSystemDirectoryRepository();

        settingsRepository = new SharedPreferencesSettingsRepository(this);
    }

    @NonNull
    public StorageRepository getStorageRepository() {
        return storageRepository;
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
