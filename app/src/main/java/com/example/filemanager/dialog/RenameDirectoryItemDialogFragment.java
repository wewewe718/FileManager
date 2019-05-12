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
import android.widget.EditText;

import com.example.filemanager.R;
import com.example.filemanager.databinding.DialogRenameDirectoryItemBinding;
import com.example.filemanager.model.DirectoryItem;

public class RenameDirectoryItemDialogFragment extends DialogFragment {

    public interface Listener {
        void onRenameDirectoryItem(@NonNull String newName, @NonNull DirectoryItem item);
    }


    private static final String DIRECTORY_ITEM_ARGUMENTS_KEY = "DIRECTORY_ITEM_ARGUMENTS_KEY";
    private Listener listener;

    @NonNull
    public static RenameDirectoryItemDialogFragment newInstance(@NonNull DirectoryItem item) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(DIRECTORY_ITEM_ARGUMENTS_KEY, item);

        RenameDirectoryItemDialogFragment fragment = new RenameDirectoryItemDialogFragment();
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
        DialogRenameDirectoryItemBinding binding = inflateDialog();

        showDirectoryItem(binding, item);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.rename_directory_item_dialog_title)
                .setView(binding.getRoot())
                .setNegativeButton(R.string.rename_directory_item_dialog_negative_button, null)
                .setPositiveButton(R.string.rename_directory_item_dialog_positive_button, (dialog, which) -> {
                    handlePositiveButtonClicked(binding, item);
                })
                .create();
    }

    @NonNull
    private DialogRenameDirectoryItemBinding inflateDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return DataBindingUtil.inflate(inflater, R.layout.dialog_rename_directory_item, null, false);
    }

    private void showDirectoryItem(@NonNull DialogRenameDirectoryItemBinding binding, @NonNull DirectoryItem item) {
        EditText nameEditText = binding.editTextNewName;
        nameEditText.setText(item.getName());
        nameEditText.setSelection(nameEditText.getText().length());
    }

    private void handlePositiveButtonClicked(@NonNull DialogRenameDirectoryItemBinding binding, @NonNull DirectoryItem item) {
        String newName = binding.editTextNewName.getText().toString();
        listener.onRenameDirectoryItem(newName, item);
    }
}
