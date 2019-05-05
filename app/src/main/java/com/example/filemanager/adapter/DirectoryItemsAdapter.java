package com.example.filemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.databinding.ItemDirectoryItemBinding;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.DirectoryItemType;
import com.example.filemanager.util.FileSizeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DirectoryItemsAdapter extends RecyclerView.Adapter<DirectoryItemsAdapter.ViewHolder> {

    public interface Listener {
        void onItemClicked(@NonNull DirectoryItem item);
    }

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat( "dd-MM-yyyy", Locale.ENGLISH);

    private List<DirectoryItem> data = Collections.emptyList();
    private Listener listener;


    public DirectoryItemsAdapter(Listener listener) {
        this.listener = listener;
    }


    public void setData(@NonNull List<DirectoryItem> data) {
        this.data = data;
        notifyDataSetChanged();
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
        viewHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDirectoryItemBinding binding;

        public ViewHolder(@NonNull ItemDirectoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(@NonNull DirectoryItem item) {
            binding.getRoot().setOnClickListener(v -> listener.onItemClicked(item));

            binding.textViewName.setText(item.getName());
            showDirectoryItemTypeImage(item);
            showDateIfNeeded(item);
            showFileSizeIfNeeded(item);
        }

        private void showDateIfNeeded(@NonNull DirectoryItem item) {
            if (!item.getType().equals(DirectoryItemType.DIRECTORY)) {
                return;
            }

            String dateText = DATE_FORMATTER.format(item.getLastModificationDate());
            binding.textViewProperty.setText(dateText);
        }

        private void showFileSizeIfNeeded(@NonNull DirectoryItem item) {
            if (item.getType().equals(DirectoryItemType.DIRECTORY)) {
                return;
            }

            Pair<FileSizeConverter.FileSizeUnit, Double> fileSizeConverterResult = FileSizeConverter.convertFileSize(item.getFileSizeInBytes());
            int formatStringId = mapFileSizeUnitToFormatString(fileSizeConverterResult.first);
            String formattedFileSize = binding.getRoot().getContext().getString(formatStringId, fileSizeConverterResult.second);
            binding.textViewProperty.setText(formattedFileSize);
        }

        private void showDirectoryItemTypeImage(@NonNull DirectoryItem item) {
            int drawable = mapDirectoryTypeToDrawable(item.getType());
            binding.imageViewItemType.setBackgroundResource(drawable);
        }

        @DrawableRes
        private int mapDirectoryTypeToDrawable(@NonNull DirectoryItemType itemType) {
            switch (itemType) {
                case DIRECTORY: return R.drawable.ic_directory;
                case IMAGE: return R.drawable.ic_image;
                case AUDIO: return R.drawable.ic_audio;
                case VIDEO: return R.drawable.ic_video;
                case TEXT: return R.drawable.ic_text_file;
                case OTHER: return R.drawable.ic_text_file;
            }
            return -1;
        }

        @StringRes
        private int mapFileSizeUnitToFormatString(FileSizeConverter.FileSizeUnit fileSizeUnit) {
            switch (fileSizeUnit) {
                case BYTE: return R.string.file_size_in_bytes;
                case KILOBYTE: return R.string.file_size_in_kb;
                case MEGABYTE: return R.string.file_size_in_mb;
                case GIGABYTE: return R.string.file_size_in_gb;
            }
            return -1;
        }
    }
}
