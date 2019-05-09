package com.example.filemanager.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Pair;

import com.example.filemanager.R;

public class FileSizeFormatUtil {

    @NonNull
    public static String formatFileSize(@NonNull Context context, long fileSizeInBytes) {
        Pair<FileSizeConverter.FileSizeUnit, Double> fileSizeConverterResult = FileSizeConverter.convertFileSize(fileSizeInBytes);
        int formatStringId = mapFileSizeUnitToFormatString(fileSizeConverterResult.first);
        return context.getString(formatStringId, fileSizeConverterResult.second);
    }

    @StringRes
    private static int mapFileSizeUnitToFormatString(FileSizeConverter.FileSizeUnit fileSizeUnit) {
        switch (fileSizeUnit) {
            case BYTE: return R.string.file_size_in_bytes;
            case KILOBYTE: return R.string.file_size_in_kb;
            case MEGABYTE: return R.string.file_size_in_mb;
            case GIGABYTE: return R.string.file_size_in_gb;
        }
        return -1;
    }
}
