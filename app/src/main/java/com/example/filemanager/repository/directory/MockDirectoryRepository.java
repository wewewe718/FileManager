package com.example.filemanager.repository.directory;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MockDirectoryRepository implements DirectoryRepository {
    @NonNull
    @Override
    public Single<List<Object>> getDirectoryContent(@NonNull String directory) {
        return Single.just(Collections.emptyList())
                .delay(5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }
}
