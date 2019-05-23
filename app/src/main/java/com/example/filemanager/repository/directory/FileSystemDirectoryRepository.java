package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.model.exception.LoadDirectoryContentException;
import com.example.filemanager.util.DirectoryItemTypeUtil;
import com.example.filemanager.util.filesystem.CopyUtil;
import com.example.filemanager.util.filesystem.CreateDirectoryUtil;
import com.example.filemanager.util.filesystem.DeleteUtil;
import com.example.filemanager.util.filesystem.DirectoryContentUtil;
import com.example.filemanager.util.filesystem.MoveUtil;
import com.example.filemanager.util.filesystem.RenameUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public class FileSystemDirectoryRepository implements DirectoryRepository {

    @NonNull
    @Override
    public Single<List<DirectoryItem>> getDirectoryContent(@NonNull String directory) {
        return Single.create(emitter -> {
            try {
                List<DirectoryItem> result = tryGetDirectoryContent(directory);
                emitter.onSuccess(result);
            } catch (Exception ex) {
                emitter.onError(new LoadDirectoryContentException(ex));
            }
        });
    }

    @NonNull
    @Override
    public Completable createDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        return createCompletable(() -> tryCreateDirectory(rootDirectoryFullPath, newDirectoryName));
    }

    @NonNull
    @Override
    public Completable moveAndCopy(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> itemsToMove, @NonNull List<DirectoryItem> itemsToCopy) {
        return createCompletable(() -> tryMoveAndCopy(targetDirectoryFullPath, itemsToMove, itemsToCopy));
    }

    @NonNull
    @Override
    public Completable rename(@NonNull String newName, @NonNull DirectoryItem item) {
        return createCompletable(() -> tryRename(newName, item));
    }

    @NonNull
    @Override
    public Completable delete(@NonNull DirectoryItem item) {
        return createCompletable(() -> tryDelete(item));
    }

    @NonNull
    @Override
    public Completable delete(@NonNull List<DirectoryItem> items) {
        return createCompletable(() -> tryDelete(items));
    }

    @NonNull
    private Completable createCompletable(@NonNull Action action) {
        return Completable.create(emitter -> {
            try {
                action.run();
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }


    @NonNull
    private List<DirectoryItem> tryGetDirectoryContent(@NonNull String directory) {
        File[] files = DirectoryContentUtil.getDirectoryContent(directory);

        List<DirectoryItem> result = new ArrayList<>();

        for (File file : files) {
            DirectoryItem item = createDirectoryItemFromFile(file);
            result.add(item);
        }

        return result;
    }

    private void tryCreateDirectory(@NonNull String rootDirectoryFullPath, @NonNull String newDirectoryName) {
        CreateDirectoryUtil.createDirectory(rootDirectoryFullPath, newDirectoryName);
    }

    private void tryMoveAndCopy(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> itemsToMove, @NonNull List<DirectoryItem> itemsToCopy) {
        tryMove(targetDirectoryFullPath, itemsToMove);
        tryCopy(targetDirectoryFullPath, itemsToCopy);
    }

    private void tryMove(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> items) {
        for (DirectoryItem item : items) {
            MoveUtil.move(item.getFilePath(), targetDirectoryFullPath);
        }
    }

    private void tryCopy(@NonNull String targetDirectoryFullPath, @NonNull List<DirectoryItem> items) {
        for (DirectoryItem item : items) {
            CopyUtil.copy(item.getFilePath(), targetDirectoryFullPath);
        }
    }

    private void tryRename(@NonNull String newName, @NonNull DirectoryItem item) {
        RenameUtil.rename(item.getFilePath(), newName);
    }

    private void tryDelete(@NonNull List<DirectoryItem> items) throws Exception {
        Exception lastError = null;

        for (DirectoryItem item : items) {
            try {
                tryDelete(item);
            } catch (Exception ex) {
                lastError = ex;
            }
        }

        if (lastError != null) {
            throw lastError;
        }
    }

    private void tryDelete(@NonNull DirectoryItem item) {
        File fileOrDirectory = new File(item.getFilePath());
        DeleteUtil.delete(fileOrDirectory);
    }


    @NonNull
    private DirectoryItem createDirectoryItemFromFile(@NonNull File file) {
        return new DirectoryItem(
                getDirectoryItemType(file),
                file.getName(),
                file.getPath(),
                file.toURI().toString(),
                getLastModifiedDate(file),
                file.length(),
                file.isHidden()
        );
    }

    @NonNull
    private DirectoryItemType getDirectoryItemType(@NonNull File file) {
        return DirectoryItemTypeUtil.getDirectoryItemType(file);
    }

    @NonNull
    private Date getLastModifiedDate(@NonNull File file) {
        return new Date(file.lastModified());
    }
}
