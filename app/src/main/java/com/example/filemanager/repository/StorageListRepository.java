package com.example.filemanager.repository;

import com.example.filemanager.model.StorageModel;
import com.example.filemanager.model.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class StorageListRepository {
    private List<StorageModel> storageModelList = new ArrayList<>();

    public StorageListRepository() {
        storageModelList.add(new StorageModel(StorageType.INTERNAL_STORAGE, "Internal storage", 9, 7));
        storageModelList.add(new StorageModel(StorageType.EXTERNAL_STORAGE, "External storage", 19, 2));
    }

    public Single<List<StorageModel>> getStorageList() {
        return Single.just(storageModelList)
                .delay(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }
}
