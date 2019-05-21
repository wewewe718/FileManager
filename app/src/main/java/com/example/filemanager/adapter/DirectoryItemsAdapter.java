package com.example.filemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.databinding.ItemDirectoryItemBinding;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.util.DateFormatUtil;
import com.example.filemanager.util.FileSizeFormatUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryItemsAdapter extends RecyclerView.Adapter<DirectoryItemsAdapter.ViewHolder> {

    public interface Listener {
        void onDirectoryItemClicked(@NonNull DirectoryItem item);
        void onDirectoryItemCutClicked(@NonNull DirectoryItem item);
        void onDirectoryItemCopyClicked(@NonNull DirectoryItem item);
        void onDirectoryItemRenameClicked(@NonNull DirectoryItem item);
        void onDirectoryItemDeleteClicked(@NonNull DirectoryItem item);
        void onDirectoryItemInfoClicked(@NonNull DirectoryItem item);
        void onDirectoryItemShareClicked(@NonNull DirectoryItem item);
        void onItemSelectionChanged(boolean isInSelectMode);
    }


    private List<DirectoryItem> data = Collections.emptyList();
    private String searchQuery = "";
    private Listener listener;
    private List<DirectoryItem> selectedItems = new ArrayList<>();


    public DirectoryItemsAdapter(Listener listener) {
        this.listener = listener;
    }


    public void setSearchQuery(@NonNull String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setData(@NonNull List<DirectoryItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void resetSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void selectAll() {
        selectedItems.clear();
        selectedItems.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    public List<DirectoryItem> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemDirectoryItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_directory_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DirectoryItem item = data.get(i);
        boolean isItemSelected = selectedItems.contains(item);
        boolean isInSelectMode = isInSelectMode();
        viewHolder.bind(item, isItemSelected, isInSelectMode);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private void handleItemClicked(@NonNull DirectoryItem item, boolean isItemSelected) {
        if (isInSelectMode()) {
            handleItemSelected(item, isItemSelected);
        } else {
            listener.onDirectoryItemClicked(item);
        }
    }

    private void handleItemLongClicked(@NonNull DirectoryItem item, boolean isItemSelected) {
        handleItemSelected(item, isItemSelected);
    }

    private void handleItemSelected(@NonNull DirectoryItem item, boolean isItemSelected) {
        isItemSelected = !isItemSelected;
        if (isItemSelected) {
            addSelectedItem(item);
        } else {
            removeSelectedItem(item);
        }
    }

    private boolean isInSelectMode() {
        return !selectedItems.isEmpty();
    }

    private void addSelectedItem(@NonNull DirectoryItem item) {
        selectedItems.add(item);
        notifyItemSelectionChanged();
        notifyDataSetChanged();
    }

    private void removeSelectedItem(@NonNull DirectoryItem item) {
        selectedItems.remove(item);
        notifyItemSelectionChanged();
        notifyDataSetChanged();
    }

    private void notifyItemSelectionChanged() {
        boolean isInSelectMode = isInSelectMode();
        listener.onItemSelectionChanged(isInSelectMode);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDirectoryItemBinding binding;

        ViewHolder(@NonNull ItemDirectoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(@NonNull DirectoryItem item, boolean isItemSelected, boolean isInSelectMode) {
            initEventHandlers(item, isItemSelected);
            showName(item);
            showTypeImage(item);
            showDateIfNeeded(item);
            showFileSizeIfNeeded(item);
            showItemSelected(isItemSelected);
            showIsInSelectMode(isInSelectMode);
        }

        private void initEventHandlers(@NonNull DirectoryItem item, boolean isItemSelected) {
            binding.imageViewMore.setOnClickListener(v -> showPopupMenu(v, item));

            binding.getRoot().setOnClickListener(v -> {
                handleItemClicked(item, isItemSelected);
            });

            binding.getRoot().setOnLongClickListener(v -> {
                handleItemLongClicked(item, isItemSelected);
                return true;
            });
        }

        private void showName(@NonNull DirectoryItem item) {
            CharSequence name;
            if (searchQuery.isEmpty()) {
                name = item.getName();
            } else {
                name = highlightText(item.getName(), searchQuery);
            }
            binding.textViewName.setText(name);
        }

        private void showTypeImage(@NonNull DirectoryItem item) {
            if (item.getType() == DirectoryItemType.IMAGE) {
                showImagePreview(item);
                return;
            }

            Resources resources = binding.getRoot().getContext().getResources();

            int drawableResId = getTypeDrawable(item.getType());
            Drawable drawable = resources.getDrawable(drawableResId);

            int tintResId = item.isHidden() ? R.color.colorAccentTransparent : R.color.colorAccent;
            int tintColor = resources.getColor(tintResId);

            DrawableCompat.setTint(drawable, tintColor);

            binding.imageViewItemType.setImageDrawable(drawable);
        }

        private void showImagePreview(@NonNull DirectoryItem item) {
            binding.imageViewItemType.clearColorFilter();

            Picasso.get()
                    .load(item.getUri())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .fit()
                    .into(binding.imageViewItemType);
        }

        private void showDateIfNeeded(@NonNull DirectoryItem item) {
            if (!item.getType().equals(DirectoryItemType.DIRECTORY)) {
                return;
            }

            String dateText = DateFormatUtil.formatDate(item.getLastModificationDate());
            binding.textViewProperty.setText(dateText);
        }

        private void showFileSizeIfNeeded(@NonNull DirectoryItem item) {
            if (item.getType() == DirectoryItemType.DIRECTORY) {
                return;
            }

            String formattedFileSize = FileSizeFormatUtil.formatFileSize(binding.getRoot().getContext(), item.getFileSizeInBytes());
            binding.textViewProperty.setText(formattedFileSize);
        }

        private void showItemSelected(boolean isItemSelected) {
            Resources resources = binding.getRoot().getContext().getResources();
            int backgroundColorId = isItemSelected ? R.color.light_gray : R.color.transparent;
            int backgroundColor = resources.getColor(backgroundColorId);
            binding.getRoot().setBackgroundColor(backgroundColor);
        }

        private void showIsInSelectMode(boolean isInSelectMode) {
            int moreButtonVisibility = isInSelectMode ? View.INVISIBLE : View.VISIBLE;
            binding.imageViewMore.setVisibility(moreButtonVisibility);
        }

        @DrawableRes
        private int getTypeDrawable(@NonNull DirectoryItemType itemType) {
            switch (itemType) {
                case DIRECTORY:
                    return R.drawable.ic_directory;
                case IMAGE:
                    return R.drawable.ic_image;
                case AUDIO:
                    return R.drawable.ic_audio;
                case VIDEO:
                    return R.drawable.ic_video;
                case ARCHIVE:
                    return R.drawable.ic_archive;
                case TEXT:
                    return R.drawable.ic_document;
                case OTHER:
                    return R.drawable.ic_other_file;
            }
            return -1;
        }

        @SuppressLint("RestrictedApi")
        private void showPopupMenu(@NonNull View view, @NonNull DirectoryItem item) {
            Context context = itemView.getContext();

            PopupMenu popup = new PopupMenu(context, view);
            MenuInflater inflater = popup.getMenuInflater();
            Menu popupMenu = popup.getMenu();
            inflater.inflate(R.menu.menu_directory_item, popupMenu);

            popup.setOnMenuItemClickListener(menuItem -> {
                handlePopupMenuItemClicked(menuItem.getItemId(), item);
                return true;
            });

            if (item.getType().equals(DirectoryItemType.DIRECTORY)) {
                popupMenu.removeItem(R.id.item_share);
            }

            MenuPopupHelper menuHelper = new MenuPopupHelper(context, (MenuBuilder) popupMenu, view);
            menuHelper.setForceShowIcon(true);
            menuHelper.show();
        }

        private void handlePopupMenuItemClicked(@IdRes int itemId, @NonNull DirectoryItem item) {
            switch (itemId) {
                case R.id.item_cut: {
                    listener.onDirectoryItemCutClicked(item);
                    break;
                }
                case R.id.item_copy: {
                    listener.onDirectoryItemCopyClicked(item);
                    break;
                }
                case R.id.item_rename: {
                    listener.onDirectoryItemRenameClicked(item);
                    break;
                }
                case R.id.item_delete: {
                    listener.onDirectoryItemDeleteClicked(item);
                    break;
                }
                case R.id.item_info: {
                    listener.onDirectoryItemInfoClicked(item);
                    break;
                }
                case R.id.item_share: {
                    listener.onDirectoryItemShareClicked(item);
                    break;
                }
            }
        }

        private SpannableString highlightText(@NonNull String text, @NonNull String searchText) {
            SpannableString result = new SpannableString(text);

            // Remove previous spans
            BackgroundColorSpan[] backgroundSpans = result.getSpans(0, result.length(), BackgroundColorSpan.class);
            for (BackgroundColorSpan span : backgroundSpans) {
                result.removeSpan(span);
            }

            searchText = searchText.toLowerCase();
            text = text.toLowerCase();

            // Highlight all searchText occurrences
            int indexOfKeyword = text.indexOf(searchText);
            while (indexOfKeyword >= 0) {
                //Create a background color span on the keyword
                result.setSpan(new BackgroundColorSpan(Color.YELLOW), indexOfKeyword, indexOfKeyword + searchText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                //Get the next index of the keyword
                indexOfKeyword = text.indexOf(searchText, indexOfKeyword + searchText.length());
            }

            return result;
        }
    }
}
