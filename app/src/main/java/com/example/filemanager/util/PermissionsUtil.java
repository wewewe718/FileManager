package com.example.filemanager.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionsUtil {

    public static void requestPermissions(@NonNull AppCompatActivity activity, @NonNull String[] permissions, int permissionsRequestCode) {
        if (permissions.length == 0) {
            return;
        }

        String[] notGrantedPermissions = filterNotGrantedPermissions(activity, permissions);
        if (notGrantedPermissions.length == 0) {
            return;
        }

        ActivityCompat.requestPermissions(activity, notGrantedPermissions, permissionsRequestCode);
    }

    @NonNull
    private static String[] filterNotGrantedPermissions(@NonNull Context context, @NonNull String[] permissions) {
        List<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }
        return notGrantedPermissions.toArray(new String[0]);
    }

    @NonNull
    public static Pair<List<String>, List<String>> handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int permissionsRequestCode) {
        if (requestCode != permissionsRequestCode) {
            return new Pair<>(Collections.emptyList(), Collections.emptyList());
        }

        List<String> grantedPermissions = new ArrayList<>();
        List<String> notGrantedPermissions = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            int grantResult = grantResults[i];
            String permission = permissions[i];

            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permission);
            } else {
                notGrantedPermissions.add(permission);
            }
        }

        return new Pair<>(grantedPermissions, notGrantedPermissions);
    }
}
