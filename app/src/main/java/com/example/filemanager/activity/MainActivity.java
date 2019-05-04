package com.example.filemanager.activity;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.filemanager.R;
import com.example.filemanager.adapter.QuickAccessItemsAdapter;
import com.example.filemanager.adapter.StorageItemsAdapter;
import com.example.filemanager.databinding.ActivityMainBinding;
import com.example.filemanager.model.StorageModel;
import com.example.filemanager.repository.StorageListRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private static final String STORAGE_ITEMS_RECYCLER_VIEW_VISIBILITY_KEY = "STORAGE_ITEMS_VISIBILITY_KEY";
    private static final String STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY = "STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY";

    private ActivityMainBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StorageListRepository storageListRepository = new StorageListRepository();
    private StorageItemsAdapter adapter = new StorageItemsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loadStorageItems();
        initQuickAccessItemsView();
        initStorageItemsRecyclerView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(saveInstanceState(outState));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreInstanceState(savedInstanceState);
    }


    private Bundle saveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STORAGE_ITEMS_RECYCLER_VIEW_VISIBILITY_KEY, binding.storageItemsRecyclerView.getVisibility());
        outState.putInt(STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY, binding.storageItemsLoadingProgressBar.getVisibility());
        return outState;
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        binding.storageItemsRecyclerView.setVisibility(savedInstanceState.getInt(STORAGE_ITEMS_RECYCLER_VIEW_VISIBILITY_KEY, View.GONE));
        binding.storageItemsLoadingProgressBar.setVisibility(savedInstanceState.getInt(STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY, View.GONE));
    }


    private void initQuickAccessItemsView() {
        // Calculate number of columns
        int columnNumber = 3;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnNumber = 5;
        }

        binding.quickAccessItemsGridView.setNumColumns(columnNumber);
        binding.quickAccessItemsGridView.setAdapter(new QuickAccessItemsAdapter(this, () -> {}));
    }

    private void initStorageItemsRecyclerView() {
        binding.storageItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.storageItemsRecyclerView.setAdapter(adapter);
    }


    private void showStorageItems(@NonNull List<StorageModel> storageModels) {
        binding.storageItemsLoadingProgressBar.setVisibility(View.GONE);
        binding.storageItemsRecyclerView.setVisibility(View.VISIBLE);
        adapter.setData(storageModels);
    }


    private void loadStorageItems() {
        Disposable disposable = storageListRepository.getStorageList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::showStorageItems,
                        Throwable::printStackTrace
                );

        compositeDisposable.add(disposable);
    }
}
