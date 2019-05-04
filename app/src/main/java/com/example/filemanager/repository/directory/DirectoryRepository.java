package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;
import java.util.List;
import io.reactivex.Single;

public interface DirectoryRepository {
    @NonNull
    Single<List<Object>> getDirectoryContent(@NonNull String directory);
}
