package com.example.filemanager.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.View;

import com.example.App;
import com.example.filemanager.R;
import com.example.filemanager.adapter.QuickAccessItemsAdapter;
import com.example.filemanager.adapter.StorageItemsAdapter;
import com.example.filemanager.databinding.ActivityMainBinding;
import com.example.filemanager.model.StorageModel;
import com.example.filemanager.repository.storage.StorageRepository;
import com.example.filemanager.util.PermissionsUtil;
import com.example.filemanager.viewmodel.StorageListViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final String[] PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private CompositeDisposable viewModelDisposable = new CompositeDisposable();
    private StorageItemsAdapter adapter = new StorageItemsAdapter(this::showStorageDirectoryActivity);
    private StorageListViewModel viewModel;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        requestPermissions();

        initQuickAccessItemsView();
        initStorageItemsRecyclerView();

        createViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribeToViewModel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeFromViewModel();
    }


    private void requestPermissions() {
        PermissionsUtil.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Pair<List<String>, List<String>> result = PermissionsUtil.handlePermissionsResult(requestCode, permissions, grantResults, PERMISSION_REQUEST_CODE);
        List<String> notGrantedPermissions = result.second;
        if (!notGrantedPermissions.isEmpty()) {
            finish();
        }
    }

    private void initQuickAccessItemsView() {
        int columnNumber = 3;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnNumber = 5;
        }

        binding.quickAccessItemsGridView.setNumColumns(columnNumber);
        binding.quickAccessItemsGridView.setAdapter(new QuickAccessItemsAdapter(this, this::showQuickAccessItemDirectoryActivity));
    }

    private void initStorageItemsRecyclerView() {
        binding.storageItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.storageItemsRecyclerView.setAdapter(adapter);
    }


    private void createViewModel() {
        App app = (App) getApplication();
        StorageRepository storageRepository = app.getStorageRepository();
        ViewModelProvider.Factory viewModelFactory = new StorageListViewModel.Factory(storageRepository);

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(StorageListViewModel.class);
    }

    private void subscribeToViewModel() {
        viewModelDisposable.addAll(
                viewModel.isLoading.subscribe(this::showIsLoading),
                viewModel.storageList.subscribe(this::showStorageItems)
        );
    }

    private void unsubscribeFromViewModel() {
        viewModelDisposable.clear();
    }


    private void showIsLoading(boolean isLoading) {
        binding.storageItemsRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.storageItemsLoadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showStorageItems(@NonNull List<StorageModel> storageModels) {
        adapter.setData(storageModels);
    }


    private void showQuickAccessItemDirectoryActivity(@NonNull String directoryPath) {
        DirectoryActivity.start(this, directoryPath);
    }

    private void showStorageDirectoryActivity(@NonNull StorageModel storageModel) {
        DirectoryActivity.start(this, storageModel.getName());
    }
}
