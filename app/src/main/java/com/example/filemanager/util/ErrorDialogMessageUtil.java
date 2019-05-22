package com.example.filemanager.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.example.filemanager.R;
import com.example.filemanager.model.exception.CreateDirectoryException;
import com.example.filemanager.model.exception.DeleteDirectoryException;
import com.example.filemanager.model.exception.DeleteFileException;
import com.example.filemanager.model.exception.DirectoryWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.FileDoesNotExistException;
import com.example.filemanager.model.exception.FileWithThisNameAlreadyExistsException;
import com.example.filemanager.model.exception.LoadDirectoryContentException;
import com.example.filemanager.model.exception.LoadStoragesException;
import com.example.filemanager.model.exception.RenameFileException;

import java.util.HashMap;
import java.util.Map;

public class ErrorDialogMessageUtil {
    private static Map<Class, Integer> errorMap = new HashMap<>();

    static {
        errorMap.put(CreateDirectoryException.class, R.string.error_create_directory);
        errorMap.put(DeleteDirectoryException.class, R.string.error_delete_directory);
        errorMap.put(DeleteFileException.class, R.string.error_delete_file);
        errorMap.put(DirectoryWithThisNameAlreadyExistsException.class, R.string.error_directory_with_this_name_already_exists);
        errorMap.put(FileDoesNotExistException.class, R.string.error_file_does_not_exist);
        errorMap.put(FileWithThisNameAlreadyExistsException.class, R.string.error_file_with_this_name_already_exists);
        errorMap.put(LoadDirectoryContentException.class, R.string.error_load_directory_content);
        errorMap.put(LoadStoragesException.class, R.string.error_load_storages);
        errorMap.put(RenameFileException.class, R.string.error_rename_file);
    }

    @StringRes
    public static int getErrorMessageStringId(@NonNull Throwable error) {
        Integer errorMessageId = errorMap.get(error.getClass());
        if (errorMessageId != null) {
            return errorMessageId;
        }
        return R.string.error_unknown;
    }
}
