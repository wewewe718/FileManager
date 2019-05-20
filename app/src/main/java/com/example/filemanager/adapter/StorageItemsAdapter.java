package com.example.filemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.databinding.ItemStorageBinding;
import com.example.filemanager.model.StorageModel;
import com.example.filemanager.util.FileSizeConverter;
import com.example.filemanager.util.FileSizeFormatUtil;

import java.util.Collections;
import java.util.List;


public class StorageItemsAdapter extends RecyclerView.Adapter<StorageItemsAdapter.ViewHolder> {

    public interface Listener {
        void onStorageClicked(@NonNull StorageModel storageModel);
    }


    private List<StorageModel> data = Collections.emptyList();
    private Listener listener;


    public StorageItemsAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }


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
        StorageModel storage = data.get(i);
        viewHolder.bind(storage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemStorageBinding binding;

        private ViewHolder(@NonNull ItemStorageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(@NonNull StorageModel model) {
            showStorageName(model);
            showStorageSpaceInTextView(model);
            showStorageSpaceInProgressBar(model);

            itemView.setOnClickListener(v -> listener.onStorageClicked(model));
        }

        private void showStorageName(@NonNull StorageModel model) {
            binding.storageTypeTextView.setText(model.getPath());
        }

        private void showStorageSpaceInTextView(@NonNull StorageModel model) {
            long totalSpace = model.getTotalSpace();
            long usedSpace = model.getUsedSpace();

            String formattedString = FileSizeFormatUtil.formatTwoFileSizes(
                    binding.getRoot().getContext(),
                    usedSpace,
                    totalSpace
            );
            binding.storageSpaceTextView.setText(formattedString);
        }

        private void showStorageSpaceInProgressBar(@NonNull StorageModel model) {
            Pair<FileSizeConverter.FileSizeUnit, Double> result = FileSizeConverter.convertFileSize(model.getUsedSpace());
            double usedSpace = result.second;

            FileSizeConverter.FileSizeUnit fileSizeUnit = result.first;
            double totalSpace = FileSizeConverter.convertFileSize(model.getTotalSpace(), fileSizeUnit);

            binding.storageSpaceProgressBar.setMax((int) totalSpace);
            binding.storageSpaceProgressBar.setProgress((int) usedSpace);
        }
    }
}
