package com.example.filemanager.fragment;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.filemanager.R;
import com.example.filemanager.databinding.DialogDirectoryItemInfoBinding;
import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.util.DateFormatUtil;
import com.example.filemanager.util.FileSizeFormatUtil;

public class DirectoryItemInfoDialogFragment extends DialogFragment {
    private static final String DIRECTORY_ITEM_ARGUMENTS_KEY = "DIRECTORY_ITEM_ARGUMENTS_KEY";

    public static DirectoryItemInfoDialogFragment newInstance(@NonNull DirectoryItem item) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY, item);

        DirectoryItemInfoDialogFragment fragment = new DirectoryItemInfoDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DirectoryItem item = (DirectoryItem) getArguments().getSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY);
        DialogDirectoryItemInfoBinding binding = inflateDialog();

        showDirectoryItem(binding, item);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.directory_item_info_dialog_title)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.directory_item_info_positive_button, null)
                .create();
    }

    @NonNull
    private DialogDirectoryItemInfoBinding inflateDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return DataBindingUtil.inflate(inflater, R.layout.dialog_directory_item_info, null, false);
    }

    private void showDirectoryItem(@NonNull DialogDirectoryItemInfoBinding binding, @NonNull DirectoryItem item) {
        binding.textViewName.setText(item.getName());
        binding.textViewFullPath.setText(item.getFilePath());

        String formattedFileSize = FileSizeFormatUtil.formatFileSize(getContext(), item.getFileSizeInBytes());
        binding.textViewSize.setText(formattedFileSize);

        String formattedLastModificationDate = DateFormatUtil.formatDate(item.getLastModificationDate());
        binding.textViewLastModificationDate.setText(formattedLastModificationDate);
    }
}
