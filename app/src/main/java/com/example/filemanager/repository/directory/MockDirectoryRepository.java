package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MockDirectoryRepository implements DirectoryRepository {

    @NonNull
    @Override
    public Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory) {
        return Single.just(createMockDirectoryItemList(directory))
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable renameDirectoryItem(@NonNull String newName, @NonNull DirectoryItem item) {
        return Completable.complete()
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable deleteDirectoryItem(@NonNull DirectoryItem item) {
        return Completable.complete()
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<DirectoryItem> createMockDirectoryItemList(@NonNull String directory) {
        List<DirectoryItem> directoryItems = new ArrayList<>();
        directoryItems.add(new DirectoryItem(DirectoryItemType.DIRECTORY, "directory", directory + "/child", new Date(), 0));
        directoryItems.add(new DirectoryItem(DirectoryItemType.IMAGE, "picture", "", new Date(), 10));
        directoryItems.add(new DirectoryItem(DirectoryItemType.AUDIO, "audio", "", new Date(), 20 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.VIDEO, "video", "", new Date(), 30 * 1024 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.TEXT, "text", "", new Date(), 40L * 1024 * 1024 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.OTHER, "other", "", new Date(), 50));
        return directoryItems;
    }
}
