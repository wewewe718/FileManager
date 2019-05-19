package com.example.filemanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.filemanager.R;
import com.example.filemanager.databinding.DialogRenameDirectoryItemBinding;

public class CreateDirectoryDialogFragment extends DialogFragment {

    public interface Listener {
        void onCreateDirectory(@NonNull String directoryName);
    }

    private Listener listener;

    @NonNull
    public static CreateDirectoryDialogFragment newInstance() {
        return new CreateDirectoryDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogRenameDirectoryItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.dialog_rename_directory_item,
                null,
                false
        );

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.create_directory_dialog_title)
                .setView(binding.getRoot())
                .setNegativeButton(R.string.create_directory_dialog_negative_button, null)
                .setPositiveButton(R.string.create_directory_dialog_positive_button, (dialog, which) -> {
                    String directoryName = binding.editTextNewName.getText().toString();
                    if (!directoryName.isEmpty()) {
                        listener.onCreateDirectory(directoryName);
                    }
                })
                .create();
    }
}
