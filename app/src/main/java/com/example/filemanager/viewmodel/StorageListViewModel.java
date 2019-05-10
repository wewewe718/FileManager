package com.example.filemanager.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.filemanager.model.StorageModel;
import com.example.filemanager.repository.storage.MockStorageListRepository;
import com.example.filemanager.repository.storage.StorageListRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class StorageListViewModel extends ViewModel {
    private StorageListRepository storageListRepository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public Subject<Boolean> isLoading = BehaviorSubject.createDefault(true);
    public Subject<List<StorageModel>> storageList = BehaviorSubject.create();

    public StorageListViewModel(@NonNull StorageListRepository storageListRepository) {
        this.storageListRepository = storageListRepository;
        loadStorageList();
    }

    private void loadStorageList() {
        isLoading.onNext(true);

        Disposable subscription = storageListRepository
                .getStorageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            isLoading.onNext(false);
                            storageList.onNext(result);
                        },
                        error -> {
                            isLoading.onNext(false);
                            error.printStackTrace();
                        }
                );

        disposable.add(subscription);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }


    public static class Factory implements ViewModelProvider.Factory {
        private StorageListRepository storageListRepository;

        public Factory(@NonNull StorageListRepository storageListRepository) {
            this.storageListRepository = storageListRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StorageListViewModel(storageListRepository);
        }
    }
}
