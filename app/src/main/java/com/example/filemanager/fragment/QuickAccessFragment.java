package com.example.filemanager.fragment;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.adapter.QuickAccessItemsAdapter;
import com.example.filemanager.adapter.StorageItemsAdapter;
import com.example.filemanager.databinding.FragmentQuickAccessBinding;
import com.example.filemanager.model.StorageModel;
import com.example.filemanager.repository.StorageListRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class QuickAccessFragment extends BaseFragment {
    private static final String STORAGE_ITEMS_RECYCLER_VIEW_VISIBILITY_KEY = "STORAGE_ITEMS_VISIBILITY_KEY";
    private static final String STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY = "STORAGE_ITEMS_PROGRESS_BAR_VISIBILITY_KEY";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StorageListRepository storageListRepository = new StorageListRepository();
    private StorageItemsAdapter adapter = new StorageItemsAdapter();
    private FragmentQuickAccessBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadStorageItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quick_access, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initQuickAccessItemsView();
        initStorageItemsRecyclerView();

        restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(saveInstanceState(outState));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
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
        binding.quickAccessItemsGridView.setAdapter(new QuickAccessItemsAdapter(getContext(), () -> {}));
    }

    private void initStorageItemsRecyclerView() {
        binding.storageItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
