package com.example.filemanager.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.filemanager.R;

public class SortTypeDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] choices = getActivity().getResources().getStringArray(R.array.sort_types);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sort_type_dialog_title)
                .setSingleChoiceItems(choices, 0, (dialog, which) -> {})
                .setPositiveButton(R.string.sort_type_dialog_positive_button, (dialog, id) -> {})
                .create();
    }
}
