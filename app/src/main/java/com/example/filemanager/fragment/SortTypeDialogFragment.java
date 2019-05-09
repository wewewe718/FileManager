package com.example.filemanager.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.filemanager.R;

public class SortTypeDialogFragment extends DialogFragment {
    private static final String SORT_TYPE_SHARED_PREFERENCES_KEY = "SORT_TYPE_SHARED_PREFERENCES_KEY";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] choices = getActivity().getResources().getStringArray(R.array.sort_types);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sort_type_dialog_title)
                .setSingleChoiceItems(choices, getSortType(), (dialog, which) -> {})
                .setPositiveButton(R.string.sort_type_dialog_positive_button, (dialog, id) -> {
                    handlePositiveButtonClicked(dialog);
                })
                .create();
    }

    private void handlePositiveButtonClicked(@NonNull DialogInterface dialog) {
        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
        saveSortType(selectedPosition);
    }

    private int getSortType() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getInt(SORT_TYPE_SHARED_PREFERENCES_KEY, 0);
    }

    private void saveSortType(int sortType) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit()
                .putInt(SORT_TYPE_SHARED_PREFERENCES_KEY, sortType)
                .apply();
    }
}
