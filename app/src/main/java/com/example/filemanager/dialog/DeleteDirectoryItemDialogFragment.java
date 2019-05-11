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

public class DeleteDirectoryItemDialogFragment extends DialogFragment {

    public interface Listener {
        void onDeleteDirectoryItem(@NonNull DirectoryItem item);
    }


    private static final String DIRECTORY_ITEM_ARGUMENTS_KEY = "DIRECTORY_ITEM_ARGUMENTS_KEY";
    private Listener listener;

    @NonNull
    public static DeleteDirectoryItemDialogFragment newInstance(@NonNull DirectoryItem item) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY, item);

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
        DirectoryItem item = (DirectoryItem) getArguments().getSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY);
        String message = getString(R.string.delete_directory_item_dialog_message, item.getName());

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_directory_item_dialog_title)
                .setMessage(message)
                .setNegativeButton(R.string.delete_directory_item_dialog_negative_button, null)
                .setPositiveButton(R.string.delete_directory_item_dialog_positive_button, (dialog, which) -> {
                    listener.onDeleteDirectoryItem(item);
                })
                .create();
    }
}
