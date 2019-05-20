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
        int formatStringId = getFormatStringForOneFileSize(fileSizeConverterResult.first);
        return context.getString(formatStringId, fileSizeConverterResult.second);
    }

    @NonNull
    public static String formatTwoFileSizes(@NonNull Context context, long smallerFileSizeInBytes, long biggerFileSizeInBytes) {
        Pair<FileSizeConverter.FileSizeUnit, Double> result = FileSizeConverter.convertFileSize(smallerFileSizeInBytes);
        FileSizeConverter.FileSizeUnit fileSizeUnit = result.first;

        double convertedSmallerFileSize = result.second;
        double convertedBiggerFileSize = FileSizeConverter.convertFileSize(biggerFileSizeInBytes, fileSizeUnit);

        int formatStringId = getFormatStringForTwoFileSizes(fileSizeUnit);
        return context.getString(formatStringId, convertedSmallerFileSize, convertedBiggerFileSize);
    }

    @StringRes
    private static int getFormatStringForOneFileSize(FileSizeConverter.FileSizeUnit fileSizeUnit) {
        switch (fileSizeUnit) {
            case BYTE: return R.string.file_size_in_bytes;
            case KILOBYTE: return R.string.file_size_in_kb;
            case MEGABYTE: return R.string.file_size_in_mb;
            case GIGABYTE: return R.string.file_size_in_gb;
        }
        return -1;
    }

    @StringRes
    private static int getFormatStringForTwoFileSizes(FileSizeConverter.FileSizeUnit fileSizeUnit) {
        switch (fileSizeUnit) {
            case BYTE: return R.string.file_sizes_in_bytes;
            case KILOBYTE: return R.string.file_sizes_in_kb;
            case MEGABYTE: return R.string.file_sizes_in_mb;
            case GIGABYTE: return R.string.file_sizes_in_gb;
        }
        return -1;
    }
}
