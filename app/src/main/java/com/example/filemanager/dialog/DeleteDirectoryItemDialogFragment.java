package com.example.filemanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.filemanager.R;
import com.example.filemanager.model.DirectoryItem;

import java.util.List;

public class DeleteDirectoryItemDialogFragment extends DialogFragment {

    public interface Listener {
        void onDeleteDirectoryItem(@NonNull DirectoryItem item);
        void onDeleteDirectoryItems();
    }


    private static final String DIRECTORY_ITEM_ARGUMENTS_KEY = "DIRECTORY_ITEM_ARGUMENTS_KEY";
    private static final String DIRECTORY_ITEM_COUNT_ARGUMENTS_KEY = "DIRECTORY_ITEM_COUNT_ARGUMENTS_KEY";
    private Listener listener;


    @NonNull
    public static DeleteDirectoryItemDialogFragment newInstance(@NonNull DirectoryItem item) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY, item);

        DeleteDirectoryItemDialogFragment fragment = new DeleteDirectoryItemDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    public static DeleteDirectoryItemDialogFragment newInstance(@NonNull List<DirectoryItem> items) {
        Bundle arguments = new Bundle();
        arguments.putInt(DIRECTORY_ITEM_COUNT_ARGUMENTS_KEY, items.size());

        DeleteDirectoryItemDialogFragment fragment = new DeleteDirectoryItemDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = getContext();
        if (context == null) {
            throw new IllegalStateException("Context is null");
        }

        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new IllegalStateException("Arguments are null");
        }

        DirectoryItem item = (DirectoryItem) arguments.getSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY);
        int itemCount = arguments.getInt(DIRECTORY_ITEM_COUNT_ARGUMENTS_KEY, Integer.MIN_VALUE);
        if (item == null && itemCount == Integer.MIN_VALUE) {
            throw new IllegalStateException("No arguments");
        }

        String message;
        if (item != null) {
            message = getString(R.string.delete_directory_item_dialog_message, item.getName());
        } else {
            message = getString(R.string.delete_directory_item_count_dialog_message, itemCount);
        }

        return new AlertDialog.Builder(context)
                .setTitle(R.string.delete_directory_item_dialog_title)
                .setMessage(message)
                .setNegativeButton(R.string.delete_directory_item_dialog_negative_button, null)
                .setPositiveButton(R.string.delete_directory_item_dialog_positive_button, (dialog, which) -> {
                    if (item != null) {
                        listener.onDeleteDirectoryItem(item);
                    } else {
                        listener.onDeleteDirectoryItems();
                    }
                })
                .create();
    }
}
