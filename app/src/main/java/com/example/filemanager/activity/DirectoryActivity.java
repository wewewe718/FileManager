package com.example.filemanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.example.filemanager.R;
import com.example.filemanager.adapter.DirectoryItemsAdapter;
import com.example.filemanager.databinding.ActivityDirectoryBinding;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.viewmodel.DirectoryViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DirectoryActivity extends AppCompatActivity {
    private static final String DIRECTORY_INTENT_KEY = "DIRECTORY_INTENT_KEY";

    private CompositeDisposable viewModelDisposable = new CompositeDisposable();
    private DirectoryItemsAdapter adapter = new DirectoryItemsAdapter(this::handleDirectoryItemClicked);
    private DirectoryViewModel viewModel;
    private ActivityDirectoryBinding binding;


    public static void start(@NonNull Context context, @NonNull String directory) {
        Intent intent = new Intent(context, DirectoryActivity.class);
        intent.putExtra(DIRECTORY_INTENT_KEY, directory);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_directory);

        String directory = getIntent().getStringExtra(DIRECTORY_INTENT_KEY);

        initActionBar(directory);
        initDirectoryItemsRecyclerView();

        createViewModel(directory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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


    private void initActionBar(@NonNull String directory) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(directory);
    }

    private void initDirectoryItemsRecyclerView() {
        binding.directoryContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.directoryContentRecyclerView.setAdapter(adapter);
    }


    private void createViewModel(@NonNull String directory) {
        viewModel = ViewModelProviders
                .of(this, new DirectoryViewModel.Factory(directory))
                .get(DirectoryViewModel.class);
    }

    private void subscribeToViewModel() {
        viewModelDisposable.addAll(
            viewModel.isLoading.subscribe(this::showIsLoading),
            viewModel.directoryContent.subscribe(this::showDirectoryContent)
        );
    }

    private void unsubscribeFromViewModel() {
        viewModelDisposable.clear();
    }


    private void showIsLoading(boolean isLoading) {
        binding.directoryContentRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.directoryContentLoadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showDirectoryContent(@NonNull List<DirectoryItem> directoryContent) {
        if (directoryContent.isEmpty()) {
            showDirectoryIsEmpty();
        } else {
            adapter.setData(directoryContent);
        }
    }

    private void showDirectoryIsEmpty() {
        binding.textViewEmptyDirectory.setVisibility(View.VISIBLE);
    }


    private void handleDirectoryItemClicked(@NonNull Object item) {

    }
}
