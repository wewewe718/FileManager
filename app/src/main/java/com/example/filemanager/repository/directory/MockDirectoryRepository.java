package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.model.exception.CreateDirectoryException;
import com.example.filemanager.model.exception.DeleteDirectoryException;
import com.example.filemanager.model.exception.DeleteFileException;
import com.example.filemanager.model.exception.DirectoryWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.FileDoesNotExistException;
import com.example.filemanager.model.exception.FileWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.LoadDirectoryContentException;
import com.example.filemanager.model.exception.RenameFileException;

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
        //return Single.error(new LoadDirectoryContentException(new Exception()));
        return Single.just(createMockDirectoryItemList(directory))
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable createDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        //return Completable.error(new CreateDirectoryException());
        //return Completable.error(new DirectoryWithThisNameAlreadyExistsException());
        return Completable.complete()
                .delay(3500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable moveAndCopy(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> itemsToMove, @NonNull List<DirectoryItem> itemsToCopy) {
        return Completable.complete()
                .delay(3500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable rename(@NonNull String newName, @NonNull DirectoryItem item) {
        //return Completable.error(new FileWithThisNameAlreadyExistsException());
        //return Completable.error(new RenameFileException());
        return Completable.complete()
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable delete(@NonNull DirectoryItem item) {
        //return Completable.error(new DeleteDirectoryException());
        //return Completable.error(new DeleteFileException());
        //return Completable.error(new FileDoesNotExistException());
        return Completable.complete()
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    @Override
    public Completable delete(@NonNull List<DirectoryItem> items) {
        //return Completable.error(new DeleteDirectoryException());
        //return Completable.error(new DeleteFileException());
        //return Completable.error(new FileDoesNotExistException());
        return Completable.complete()
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private List<DirectoryItem> createMockDirectoryItemList(@NonNull String directory) {
        List<DirectoryItem> directoryItems = new ArrayList<>();
        directoryItems.add(new DirectoryItem(DirectoryItemType.DIRECTORY, "directory", directory + "/child", "", new Date(), 0, false));
        directoryItems.add(new DirectoryItem(DirectoryItemType.IMAGE, "picture", "", "", new Date(), 10, true));
        directoryItems.add(new DirectoryItem(DirectoryItemType.AUDIO, "audio", "", "", new Date(), 20 * 1024, false));
        directoryItems.add(new DirectoryItem(DirectoryItemType.VIDEO, "video", "", "", new Date(), 30 * 1024 * 1024, false));
        directoryItems.add(new DirectoryItem(DirectoryItemType.ARCHIVE, "archive", "", "", new Date(), 40L * 1024 * 1024 * 1024, false));
        directoryItems.add(new DirectoryItem(DirectoryItemType.TEXT, "text", "", "", new Date(), 40L * 1024 * 1024 * 1024, true));
        directoryItems.add(new DirectoryItem(DirectoryItemType.OTHER, "other", "", "", new Date(), 50, false));
        return directoryItems;
    }
}
