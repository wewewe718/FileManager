package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.model.exception.CreateDirectoryException;
import com.example.filemanager.model.exception.DeleteDirectoryException;
import com.example.filemanager.model.exception.DeleteFileException;
import com.example.filemanager.model.exception.DirectoryWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.FileWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.FileDoesNotExistException;
import com.example.filemanager.model.exception.LoadDirectoryContentException;
import com.example.filemanager.model.exception.RenameFileException;
import com.example.filemanager.util.DirectoryItemTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FileSystemDirectoryRepository implements DirectoryRepository {
    private static final String FILE_PATH_SEPARATOR = File.separator;

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
        return Completable.create(emitter -> {
           try {
               tryRename(newName, item);
               emitter.onComplete();
           } catch (Exception ex) {
             emitter.tryOnError(ex);
           }
        });
    }

    @NonNull
    @Override
    public Completable delete(@NonNull DirectoryItem item) {
        return Completable.create(emitter -> {
            try {
                tryDelete(item);
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @NonNull
    @Override
    public Completable delete(@NonNull List<DirectoryItem> items) {
        return Completable.create(emitter -> {
            try {
                tryDelete(items);
                emitter.onComplete();
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
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
        String newDirectoryPath = rootDirectoryFullPath + FILE_PATH_SEPARATOR + newDirectoryName;
        File dir = new File(newDirectoryPath);
        if (dir.exists()) {
            throw new DirectoryWithThisNameAlreadyExistsException();
        }

        boolean isDirectoryCreated = dir.mkdir();
        if (!isDirectoryCreated) {
            throw new CreateDirectoryException();
        }
    }

    private void tryRename(@NonNull String newName, @NonNull DirectoryItem item) {
        File file = new File(item.getFilePath());

        String newFileName = file.getParent() + FILE_PATH_SEPARATOR + newName;
        File newFile = new File(newFileName);
        if (newFile.exists()) {
            throw new FileWithThisNameAlreadyExistsException();
        }

        boolean isFileRenamed = file.renameTo(new File(newFileName));
        if (!isFileRenamed) {
            throw new RenameFileException();
        }
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
        tryDeleteFileOrDirectory(fileOrDirectory);
    }

    private void tryDeleteFileOrDirectory(@NonNull File fileOrDirectory) {
        if (!fileOrDirectory.exists()) {
            throw new FileDoesNotExistException();
        }

        if (fileOrDirectory.isDirectory()) {
            tryDeleteDirectory(fileOrDirectory);
        } else {
            tryDeleteFile(fileOrDirectory);
        }
    }

    private void tryDeleteDirectory(@NonNull File directory) {
        File[] files = directory.listFiles();
        for (File fileOrDirectory : files) {
            tryDeleteFileOrDirectory(fileOrDirectory);
        }

        boolean isDirectoryDeleted = directory.delete();
        if (!isDirectoryDeleted) {
            throw new DeleteDirectoryException();
        }
    }

    private void tryDeleteFile(@NonNull File file) {
        boolean isFileDeleted = file.delete();
        if (!isFileDeleted) {
            throw new DeleteFileException();
        }
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
