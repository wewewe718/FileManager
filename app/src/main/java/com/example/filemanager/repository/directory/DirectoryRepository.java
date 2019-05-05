package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;

import java.util.List;
import io.reactivex.Single;

public interface DirectoryRepository {
    @NonNull
    Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory);
}
