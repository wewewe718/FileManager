package com.example.filemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.databinding.ItemStorageBinding;
import com.example.filemanager.model.StorageModel;

import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class StorageItemsAdapter extends RecyclerView.Adapter<StorageItemsAdapter.ViewHolder> {
    private List<StorageModel> data = Collections.emptyList();

    public void setData(@NonNull List<StorageModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemStorageBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_storage, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemStorageBinding binding;

        ViewHolder(@NonNull ItemStorageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull StorageModel model) {
            showStorageTypeImage(model);
            showStorageName(model);
            showStorageSpaceInTextView(model);
            showStorageSpaceInProgressBar(model);
        }

        private void showStorageTypeImage(@NonNull StorageModel model) {
            int storageTypeImageId = -1;
            switch (model.getType()) {
                case INTERNAL_STORAGE: {
                    storageTypeImageId = R.drawable.ic_internal_storage;
                    break;
                }
                case EXTERNAL_STORAGE: {
                    storageTypeImageId = R.drawable.ic_external_storage;
                    break;
                }
            }

            binding.storageTypeImageView.setBackgroundResource(storageTypeImageId);
        }

        private void showStorageName(@NonNull StorageModel model) {
            binding.storageTypeTextView.setText(model.getName());
        }

        private void showStorageSpaceInTextView(@NonNull StorageModel model) {
            String storageSpaceFormatString = binding.getRoot().getResources().getString(R.string.storage_item_storage_space_format_string);
            String storageSpace = String.format(Locale.ENGLISH, storageSpaceFormatString, model.getUsedSpace(), model.getTotalSpace());
            binding.storageSpaceTextView.setText(storageSpace);
        }

        private void showStorageSpaceInProgressBar(@NonNull StorageModel model) {
            binding.storageSpaceProgressBar.setMax((int) model.getTotalSpace());
            binding.storageSpaceProgressBar.setProgress((int) model.getUsedSpace());
        }
    }
}
