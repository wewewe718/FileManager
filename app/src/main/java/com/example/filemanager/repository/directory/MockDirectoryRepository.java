package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MockDirectoryRepository implements DirectoryRepository {
    private List<DirectoryItem> directoryItems = createMockDirectoryItemList();

    @NonNull
    @Override
    public Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory) {
        return Single.just(directoryItems)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<DirectoryItem> createMockDirectoryItemList() {
        List<DirectoryItem> directoryItems = new ArrayList<>();
        directoryItems.add(new DirectoryItem(DirectoryItemType.DIRECTORY, "directory", "", new Date(), 0));
        directoryItems.add(new DirectoryItem(DirectoryItemType.IMAGE, "picture", "", new Date(), 10));
        directoryItems.add(new DirectoryItem(DirectoryItemType.AUDIO, "audio", "", new Date(), 20 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.VIDEO, "video", "", new Date(), 30 * 1024 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.TEXT, "text", "", new Date(), 40L * 1024 * 1024 * 1024));
        directoryItems.add(new DirectoryItem(DirectoryItemType.OTHER, "other", "", new Date(), 50));
        return directoryItems;
    }
}
