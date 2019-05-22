package com.example.filemanager.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.filemanager.model.StorageModel;
import com.example.filemanager.repository.storage.StorageRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class StorageListViewModel extends ViewModel {
    private StorageRepository storageRepository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public Subject<Boolean> isLoading = BehaviorSubject.createDefault(true);
    public Subject<List<StorageModel>> storageList = BehaviorSubject.create();
    public Subject<Throwable> error = PublishSubject.create();

    public StorageListViewModel(@NonNull StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
        loadStorageList();
    }

    private void loadStorageList() {
        isLoading.onNext(true);

        Disposable subscription = storageRepository
                .getStorageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            isLoading.onNext(false);
                            storageList.onNext(result);
                        },
                        error -> {
                            isLoading.onNext(false);
                            this.error.onNext(error);
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
        private StorageRepository storageRepository;

        public Factory(@NonNull StorageRepository storageRepository) {
            this.storageRepository = storageRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new StorageListViewModel(storageRepository);
        }
    }
}
