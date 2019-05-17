package com.example.filemanager.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
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
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.App;
import com.example.filemanager.R;
import com.example.filemanager.adapter.DirectoryItemsAdapter;
import com.example.filemanager.databinding.ActivityDirectoryBinding;
import com.example.filemanager.dialog.CopyDialog;
import com.example.filemanager.dialog.DeleteDirectoryItemDialogFragment;
import com.example.filemanager.dialog.DirectoryItemInfoDialogFragment;
import com.example.filemanager.dialog.RenameDirectoryItemDialogFragment;
import com.example.filemanager.dialog.SortTypeDialogFragment;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.repository.directory.DirectoryRepository;
import com.example.filemanager.repository.settings.SettingsRepository;
import com.example.filemanager.viewmodel.DirectoryViewModel;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class DirectoryActivity extends AppCompatActivity implements
        DirectoryItemsAdapter.Listener, DeleteDirectoryItemDialogFragment.Listener, RenameDirectoryItemDialogFragment.Listener, SortTypeDialogFragment.Listener {
    private static final String DIRECTORY_INTENT_KEY = "DIRECTORY_INTENT_KEY";
    private static final String SEARCH_VIEW_QUERY_KEY = "SEARCH_VIEW_QUERY_KEY";

    private CompositeDisposable viewModelDisposable = new CompositeDisposable();
    private DirectoryViewModel viewModel;

    private DirectoryItemsAdapter adapter = new DirectoryItemsAdapter(this);
    private CopyDialog copyDialog = new CopyDialog();
    private ActivityDirectoryBinding binding;

    private SearchView searchView;
    private String searchQuery = "";

    private ActionMode.Callback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;


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
        initPasteButton();

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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        String searchQuery = searchView.getQuery().toString();
        outState.putString(SEARCH_VIEW_QUERY_KEY, searchQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_VIEW_QUERY_KEY, "");
        }
    }

    @Override
    public void onBackPressed() {
        // Close search view if opened
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }

        viewModel.handleBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_directory_activity, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) searchItem.getActionView();
        initSearchView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                // Close search view if opened
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                    return true;
                }

                viewModel.handleActionBarBackPressed();

                break;
            }
            case R.id.item_sort: {
                showSortTypeDialog();
                break;
            }
            default: {
                break;
            }
        }
        return true;
    }


    @Override
    public void onDirectoryItemClicked(@NonNull DirectoryItem item) {
       viewModel.handleItemClicked(item);
    }

    @Override
    public void onDirectoryItemCutClicked(@NonNull DirectoryItem item) {
        viewModel.handleCutClicked(item);
    }

    @Override
    public void onDirectoryItemCopyClicked(@NonNull DirectoryItem item) {
        viewModel.handleCopyClicked(item);
    }

    @Override
    public void onDirectoryItemRenameClicked(@NonNull DirectoryItem item) {
        viewModel.handleRenameClicked(item);
    }

    @Override
    public void onDirectoryItemDeleteClicked(@NonNull DirectoryItem item) {
        viewModel.handleDeleteClicked(item);
    }

    @Override
    public void onDirectoryItemInfoClicked(@NonNull DirectoryItem item) {
        viewModel.handleInfoClicked(item);
    }

    @Override
    public void onDirectoryItemShareClicked(@NonNull DirectoryItem item) {
        viewModel.handleShareClicked(item);
    }

    @Override
    public void onItemSelectionChanged(boolean isInSelectMode) {
        // TODO: Refactor action mode
        showOrHideActionMode(isInSelectMode);
    }


    @Override
    public void onSortTypeChanged() {
        viewModel.handleSortTypeChanged();
    }

    @Override
    public void onRenameDirectoryItem(@NonNull String newName, @NonNull DirectoryItem item) {
        viewModel.handleRenameConfirmed(newName, item);
    }

    @Override
    public void onDeleteDirectoryItem(@NonNull DirectoryItem item) {
        viewModel.handleDeleteConfirmed(item);
    }

    @Override
    public void onDeleteDirectoryItems() {
        List<DirectoryItem> selectedItems = adapter.getSelectedItems();
        showOrHideActionMode(false);
        viewModel.handleDeleteConfirmed(selectedItems);
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

    private void initSearchView() {
        restoreSearchView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(@NonNull String query) {
                viewModel.handleSearchQueryChanged(query);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(@NonNull String query) {
                return false;
            }
        });
    }

    private void initPasteButton() {
        binding.fabPaste.setOnClickListener(v -> viewModel.handlePasteClicked());
    }


    private void createViewModel() {
        String directory = getIntent().getStringExtra(DIRECTORY_INTENT_KEY);

        App app = (App) getApplication();
        DirectoryRepository directoryRepository = app.getDirectoryRepository();
        SettingsRepository settingsRepository = app.getSettingsRepository();

        ViewModelProvider.Factory viewModelFactory = new DirectoryViewModel.Factory(
                directory,
                directoryRepository,
                settingsRepository
        );

        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(DirectoryViewModel.class);
    }

    private void subscribeToViewModel() {
        viewModelDisposable.addAll(
                viewModel.isLoading.subscribe(this::showIsLoading),
                viewModel.currentDirectory.subscribe(this::showCurrentDirectory),
                viewModel.directoryContent.subscribe(this::showDirectoryContent),
                viewModel.searchQuery.subscribe(this::showSearchQuery),
                viewModel.isCopyModeEnabled.subscribe(this::showCopyModeEnabled),
                viewModel.isCopyDialogVisible.subscribe(this::showOrHideCopyDialog),

                viewModel.openFileEvent.subscribe(this::openFile),
                viewModel.showRenameItemDialogEvent.subscribe(this::showRenameDirectoryItemDialog),
                viewModel.showDeleteItemDialogEvent.subscribe(this::showDeleteDirectoryItemDialog),
                viewModel.showDeleteItemsDialogEvent.subscribe(this::showDeleteDirectoryItemsDialog),
                viewModel.showInfoItemDialogEvent.subscribe(this::showDirectoryItemInfoDialog),
                viewModel.closeScreenEvent.subscribe(u -> closeScreen())
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
        boolean isEmpty = directoryContent.isEmpty();
        showDirectoryIsEmpty(isEmpty);

        if (!isEmpty) {
            adapter.setData(directoryContent);
        }
    }

    private void showDirectoryIsEmpty(boolean isEmpty) {
        binding.textViewEmptyDirectory.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.directoryContentRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void showSearchQuery(@NonNull String searchQuery) {
        adapter.setSearchQuery(searchQuery);
    }

    @SuppressLint("RestrictedApi")
    private void showCopyModeEnabled(boolean isCopyModeEnabled) {
        int fabVisibility = isCopyModeEnabled ? View.VISIBLE : View.GONE;
        int iconId = isCopyModeEnabled ? R.drawable.ic_clear : R.drawable.ic_arrow_back;

        binding.fabPaste.setVisibility(fabVisibility);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(iconId);
        }
    }

    private void showOrHideActionMode(boolean isActionModeEnabled) {
        if (isActionModeEnabled && actionMode == null) {
            actionMode = startActionMode(actionModeCallback);
            return;
        }

        if (!isActionModeEnabled && actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
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

    private void restoreSearchView() {
        if (searchQuery.isEmpty()) {
            return;
        }

        searchView.setIconified(false);
        searchView.setQuery(searchQuery, false);
    }

    private void showSortTypeDialog() {
        SortTypeDialogFragment dialog = new SortTypeDialogFragment();
        dialog.show(getSupportFragmentManager(), "SortTypeDialogFragment");
    }

    private void showOrHideCopyDialog(boolean show) {
        if (show) {
            copyDialog.show(this);
        } else {
            copyDialog.dismiss();
        }
    }

    private void showRenameDirectoryItemDialog(@NonNull DirectoryItem item) {
        RenameDirectoryItemDialogFragment dialog = RenameDirectoryItemDialogFragment.newInstance(item);
        dialog.show(getSupportFragmentManager(), "RenameDirectoryItemDialogFragment");
    }

    private void showDeleteDirectoryItemDialog(@NonNull DirectoryItem item) {
        DeleteDirectoryItemDialogFragment dialog = DeleteDirectoryItemDialogFragment.newInstance(item);
        dialog.show(getSupportFragmentManager(), "DeleteDirectoryItemDialogFragment");
    }

    private void showDeleteDirectoryItemsDialog(@NonNull List<DirectoryItem> items) {
        DeleteDirectoryItemDialogFragment dialog = DeleteDirectoryItemDialogFragment.newInstance(items);
        dialog.show(getSupportFragmentManager(), "DeleteDirectoryItemDialogFragment");
    }

    private void showDirectoryItemInfoDialog(@NonNull DirectoryItem item) {
        DirectoryItemInfoDialogFragment dialog = DirectoryItemInfoDialogFragment.newInstance(item);
        dialog.show(getSupportFragmentManager(), "DirectoryItemInfoDialogFragment");
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_directory_activity_action_mode, menu);
            actionMode.setTitle(R.string.select_items);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            List<DirectoryItem> selectedItems = adapter.getSelectedItems();

            switch (menuItem.getItemId()) {
                case R.id.item_action_mode_delete: {
                    viewModel.handleDeleteClicked(selectedItems);
                    return true;
                }
                case R.id.item_action_mode_cut: {
                    viewModel.handleCutClicked(selectedItems);
                    break;
                }
                case R.id.item_action_mode_copy: {
                    viewModel.handleCopyClicked(selectedItems);
                    break;
                }
                case R.id.item_action_mode_select_all: {
                    adapter.selectAll();
                    return true;
                }
            }

            showOrHideActionMode(false);

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            DirectoryActivity.this.actionMode = null;
            DirectoryActivity.this.adapter.resetSelection();
        }
    }
}
