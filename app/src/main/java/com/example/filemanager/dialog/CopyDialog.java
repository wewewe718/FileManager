package com.example.filemanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.example.filemanager.R;

public class CopyDialog {
    private Dialog dialog;

    public void show(@NonNull Context context) {
        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.copy_dialog_title)
                .setView(R.layout.dialog_copy)
                .setCancelable(false)
                .create();

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
