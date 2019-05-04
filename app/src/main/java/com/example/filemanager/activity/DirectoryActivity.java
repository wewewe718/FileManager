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
import android.view.MenuItem;
import android.view.View;

import com.example.filemanager.R;
import com.example.filemanager.databinding.ActivityDirectoryBinding;
import com.example.filemanager.viewmodel.DirectoryViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DirectoryActivity extends AppCompatActivity {
    private static final String DIRECTORY_INTENT_KEY = "DIRECTORY_INTENT_KEY";

    private CompositeDisposable viewModelDisposable = new CompositeDisposable();
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
        initActionBar();
        createViewModel();
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


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private void createViewModel() {
        String directory = getIntent().getStringExtra(DIRECTORY_INTENT_KEY);
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

    private void showDirectoryContent(@NonNull List<Object> directoryContent) {

    }
}
