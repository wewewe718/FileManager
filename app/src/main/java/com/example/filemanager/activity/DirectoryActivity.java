package com.example.filemanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.filemanager.R;
import com.example.filemanager.adapter.DirectoryItemsAdapter;
import com.example.filemanager.databinding.ActivityDirectoryBinding;
import com.example.filemanager.fragment.DirectoryItemInfoDialogFragment;
import com.example.filemanager.fragment.SortTypeDialogFragment;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.viewmodel.DirectoryViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DirectoryActivity extends AppCompatActivity implements DirectoryItemsAdapter.Listener {
    private static final String DIRECTORY_INTENT_KEY = "DIRECTORY_INTENT_KEY";

    private CompositeDisposable viewModelDisposable = new CompositeDisposable();
    private DirectoryItemsAdapter adapter = new DirectoryItemsAdapter(this);
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
        initDirectoryItemsRecyclerView();

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

    @Override
    public void onBackPressed() {
        viewModel.goToParentDirectory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_directory_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.item_search: {
                break;
            }
            case R.id.item_sort: {
                showSortTypeDialog();
                break;
            }
        }
        return true;
    }


    @Override
    public void onDirectoryItemClicked(@NonNull DirectoryItem item) {
        if (item.getType().equals(DirectoryItemType.DIRECTORY)) {
            viewModel.goToDirectory(item.getFilePath());
        } else {
            openFile(item);
        }
    }

    @Override
    public void onDirectoryItemInfoClicked(@NonNull DirectoryItem item) {
        showDirectoryItemInfoDialog(item);
    }


    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void initDirectoryItemsRecyclerView() {
        binding.directoryContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.directoryContentRecyclerView.setAdapter(adapter);
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
            viewModel.currentDirectory.subscribe(this::showCurrentDirectory),
            viewModel.directoryContent.subscribe(this::showDirectoryContent),
            viewModel.closeScreen.subscribe(f -> closeScreen())
        );
    }

    private void unsubscribeFromViewModel() {
        viewModelDisposable.clear();
    }


    private void showIsLoading(boolean isLoading) {
        binding.directoryContentRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        binding.directoryContentLoadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showCurrentDirectory(@NonNull String currentDirectory) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(currentDirectory);
        }
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

    private void openFile(@NonNull DirectoryItem item) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getFilePath()));
        if (intent.resolveActivity(getPackageManager()) == null) {
            showToast(R.string.unable_to_open_file);
        } else {
            startActivity(intent);
        }
    }

    private void closeScreen() {
        finish();
    }

    private void showToast(@StringRes int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }

    private void showSortTypeDialog() {
        new SortTypeDialogFragment().show(getSupportFragmentManager(), "");
    }

    private void showDirectoryItemInfoDialog(@NonNull DirectoryItem item) {
        DirectoryItemInfoDialogFragment.newInstance(item).show(getSupportFragmentManager(), "");
    }
}
