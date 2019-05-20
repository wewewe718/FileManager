package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FileSystemDirectoryRepository implements DirectoryRepository {
    @NonNull
    @Override
    public Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory) {
        return Single.create(emitter -> {
            try {
                List<DirectoryItem> result = tryGetDirectoryContent(directory);
                emitter.onSuccess(result);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @NonNull
    @Override
    public Completable createDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        return Completable.create(emitter -> {
            try {
                tryCreateDirectory(rootDirectoryFullPath, newDirectoryName);
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @NonNull
    @Override
    public Completable moveAndCopy(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> itemsToMove, @NonNull List<DirectoryItem> itemsToCopy) {
        return null;
    }

    @NonNull
    @Override
    public Completable rename(@NonNull String newName, @NonNull DirectoryItem item) {
        return null;
    }

    @NonNull
    @Override
    public Completable delete(@NonNull DirectoryItem item) {
        return null;
    }

    @NonNull
    @Override
    public Completable delete(@NonNull List<DirectoryItem> item) {
        return null;
    }


    @NonNull
    private List<DirectoryItem> tryGetDirectoryContent(@NonNull String directory) {
        List<DirectoryItem> result = new ArrayList<>();

        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files == null) {
            return result;
        }

        for (File file : files) {
            DirectoryItem item = createDirectoryItemFromFile(file);
            result.add(item);
        }

        return result;
    }

    private void tryCreateDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        String newDirectoryPath = String.format(Locale.ENGLISH, "%s%s%s", rootDirectoryFullPath, File.separator, newDirectoryName);
        File dir = new File(newDirectoryPath);
        boolean isDirectoryCreated = dir.mkdir();
        if (!isDirectoryCreated) {
            throw new IllegalStateException("Unable to create directory");
        }
    }

    @NonNull
    private DirectoryItem createDirectoryItemFromFile(@NonNull File file) {
        return new DirectoryItem(
                createDirectoryItemTypeFromFile(file),
                file.getName(),
                file.getPath(),
                new Date(file.lastModified()),
                file.length()
        );
    }

    @NonNull
    private DirectoryItemType createDirectoryItemTypeFromFile(@NonNull File file) {
        // TODO: Support other types
        if (file.isDirectory()) {
            return DirectoryItemType.DIRECTORY;
        } else {
            return DirectoryItemType.OTHER;
        }
    }
}
