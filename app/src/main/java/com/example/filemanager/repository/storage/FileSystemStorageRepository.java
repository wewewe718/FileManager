package com.example.filemanager.repository.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.example.filemanager.model.StorageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.Single;

import static android.os.Build.VERSION.SDK_INT;

public class FileSystemStorageRepository implements StorageRepository {
    private static final Pattern DIR_SEPARATOR = Pattern.compile("/");
    private static final String DEFAULT_FALLBACK_STORAGE_PATH = "/storage/sdcard0";

    private Context context;

    public FileSystemStorageRepository(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Single<List<StorageModel>> getStorageList() {
        return Single.create(emitter -> {
            try {
                List<StorageModel> result = tryGetStorageList();
                emitter.onSuccess(result);
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    @NonNull
    private List<StorageModel> tryGetStorageList() {
        List<StorageModel> result = new ArrayList<>();
        List<String> storages = tryGetStorages();
        for (String storagePath : storages) {
            StorageModel storage = createStorageModelFromStoragePath(storagePath);
            result.add(storage);
        }
        return result;
    }

    @NonNull
    private List<String> tryGetStorages() {
        // TODO: Refactor this method

        // Final set of paths
        List<String> result = new ArrayList<>();

        // Primary physical SD-CARD (not emulated)
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");

        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        String rawSecondaryStorages = System.getenv("SECONDARY_STORAGE");

        // Primary emulated SD-CARD
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");

        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                // Check for actual existence of the directory before adding to list
                if (new File(DEFAULT_FALLBACK_STORAGE_PATH).exists()) {
                    result.add(DEFAULT_FALLBACK_STORAGE_PATH);
                } else {
                    //We know nothing else, use Environment's fallback
                    result.add(Environment.getExternalStorageDirectory().getAbsolutePath());
                }
            } else {
                result.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            String rawUserId;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] folders = DIR_SEPARATOR.split(path);
            String lastFolder = folders[folders.length - 1];

            boolean isDigit = false;
            try {
                Integer.valueOf(lastFolder);
                isDigit = true;
            } catch (NumberFormatException ignored) {
            }

            rawUserId = isDigit ? lastFolder : "";

            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                result.add(rawEmulatedStorageTarget);
            } else {
                result.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }

        if (SDK_INT >= Build.VERSION_CODES.M && checkStoragePermission()) {
            result.clear();
        }

        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStorages)) {
            // All Secondary SD-CARDs splited into array
            String[] storages = rawSecondaryStorages.split(File.pathSeparator);
            Collections.addAll(result, storages);
        }

        String externalSdCardPaths[] = getExternalSdCardPaths();
        for (String path : externalSdCardPaths) {
            File file = new File(path);
            if (!result.contains(path) && canListFiles(file)) {
                result.add(path);
            }
        }

        File usb = getUsbDrive();
        if (usb != null && !result.contains(usb.getPath())) {
            result.add(usb.getPath());
        }

        // TODO: Support OTG
//        if (SingletonUsbOtg.getInstance().isDeviceConnected()) {
//            rv.add(OTGUtil.PREFIX_OTG + "/");
//        }

        return result;
    }

    private String[] getExternalSdCardPaths() {
        List<String> paths = new ArrayList<>();

        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    //Log.w(LOG, "Unexpected external file dir: " + file.getAbsolutePath());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                        // Keep non-canonical path.
                    }
                    paths.add(path);
                }
            }
        }

        if (paths.isEmpty()) {
            paths.add("/storage/sdcard1");
        }

        return paths.toArray(new String[0]);
    }

    private boolean canListFiles(File file) {
        return file.canRead() && file.isDirectory();
    }

    private File getUsbDrive() {
        File parent = new File("/storage");

        try {
            for (File file : parent.listFiles()) {
                if (file.exists() && file.getName().toLowerCase().contains("usb") && file.canExecute()) {
                    return file;
                }
            }
        } catch (Exception ignore) {
        }

        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute()) {
            return parent;
        }

        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute()) {
            return parent;
        }

        return null;
    }

    private boolean checkStoragePermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @NonNull
    private StorageModel createStorageModelFromStoragePath(@NonNull String storagePath) {
        File file = new File(storagePath);

        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        return new StorageModel(
                storagePath,
                totalSpace,
                usedSpace
        );
    }
}
