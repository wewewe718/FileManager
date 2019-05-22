package com.example.filemanager.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.filemanager.R;
import com.example.filemanager.util.ErrorDialogMessageUtil;

public class ErrorDialogFragment extends DialogFragment {
    private static final String ERROR_MESSAGE_ID_KEY = "ERROR_MESSAGE_ID_KEY";

    public static ErrorDialogFragment newInstance(@NonNull Throwable error) {
        int errorMessageId = ErrorDialogMessageUtil.getErrorMessageStringId(error);

        Bundle arguments = new Bundle();
        arguments.putInt(ERROR_MESSAGE_ID_KEY, errorMessageId);

        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        int errorMessageId = R.string.error_unknown;
        if (arguments != null) {
            errorMessageId = getArguments().getInt(ERROR_MESSAGE_ID_KEY, R.string.error_unknown);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.error_dialog_title)
                .setMessage(errorMessageId)
                .setPositiveButton(R.string.error_dialog_positive_button, null)
                .create();
    }
}
