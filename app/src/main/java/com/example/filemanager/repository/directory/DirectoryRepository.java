package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface DirectoryRepository {
    @NonNull
    Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory);

    @NonNull
    Completable moveAndCopy(@NonNull String targetDirectory, @NonNull List<DirectoryItem> itemsToMove, @NonNull List<DirectoryItem> itemsToCopy);

    @NonNull
    Completable rename(@NonNull String newName, @NonNull DirectoryItem item);

    @NonNull
    Completable delete(@NonNull DirectoryItem item);

    @NonNull
    Completable delete(@NonNull List<DirectoryItem> item);
}
