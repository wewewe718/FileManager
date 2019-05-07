package com.example.filemanager.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.repository.directory.DirectoryRepository;
import com.example.filemanager.repository.directory.MockDirectoryRepository;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class DirectoryViewModel extends ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DirectoryRepository directoryRepository = new MockDirectoryRepository();

    private Stack<String> directories = new Stack<>();

    public Subject<Boolean> isLoading = BehaviorSubject.createDefault(true);
    public Subject<String> currentDirectory = BehaviorSubject.create();
    public Subject<List<DirectoryItem>> directoryContent = BehaviorSubject.create();

    public Subject<Boolean> closeScreen = PublishSubject.create();


    public DirectoryViewModel(@NonNull String directory) {
        goToDirectory(directory);
    }


    public void goToParentDirectory() {
        String parentDirectory;

        try {
            directories.pop();
            parentDirectory = directories.pop();
        } catch (EmptyStackException ex) {
            parentDirectory = null;
        }

        if (parentDirectory == null) {
            closeScreen.onNext(true);
            return;
        }

        goToDirectory(parentDirectory);
    }

    public void goToDirectory(@NonNull String directory) {
        directories.push(directory);
        this.currentDirectory.onNext(directory);
        loadDirectoryContent(directory);
    }


    private void loadDirectoryContent(@NonNull String directory) {
        isLoading.onNext(true);

        Disposable subscription = directoryRepository
                .getDirectoryContent(directory)
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
