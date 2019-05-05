package com.example.filemanager.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.repository.directory.DirectoryRepository;
import com.example.filemanager.repository.directory.MockDirectoryRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class DirectoryViewModel extends ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DirectoryRepository directoryRepository = new MockDirectoryRepository();

    private String currentDirectory;

    public Subject<Boolean> isLoading = BehaviorSubject.createDefault(true);
    public Subject<List<DirectoryItem>> directoryContent = BehaviorSubject.create();

    public DirectoryViewModel(@NonNull String directory) {
        currentDirectory = directory;
        loadDirectoryContent();
    }

    private void loadDirectoryContent() {
        isLoading.onNext(true);

        Disposable subscription = directoryRepository
                .getDirectoryContent(currentDirectory)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            isLoading.onNext(false);
                            directoryContent.onNext(result);
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
        private String directory;

        public Factory(@NonNull String directory) {
            this.directory = directory;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DirectoryViewModel(directory);
        }
    }
}
