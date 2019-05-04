package com.example.filemanager.repository.storage;

import android.support.annotation.NonNull;
import com.example.filemanager.model.StorageModel;
import java.util.List;
import io.reactivex.Single;

public interface StorageListRepository {
    @NonNull
    Single<List<StorageModel>> getStorageList();
}
