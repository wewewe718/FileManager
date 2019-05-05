package com.example.filemanager.util;

import android.util.Pair;

public class FileSizeConverter {
    public enum FileSizeUnit {
        BYTE,
        KILOBYTE,
        MEGABYTE,
        GIGABYTE,
    }

    private static final long ONE_KB = 1024;
    private static final long ONE_MB = 1024 * ONE_KB;
    private static final long ONE_GB = 1024 * ONE_MB;

    public static Pair<FileSizeUnit, Double> convertFileSize(long fileSizeInBytes) {
        double sizeInBytes = (double) fileSizeInBytes;

        double sizeInGb = sizeInBytes / ONE_GB;
        if (sizeInGb > 1) {
            return new Pair<>(FileSizeUnit.GIGABYTE, sizeInGb);
        }

        double sizeInMb = sizeInBytes / ONE_MB;
        if (sizeInMb > 1) {
            return new Pair<>(FileSizeUnit.MEGABYTE, sizeInMb);
        }

        double sizeInKb = sizeInBytes / ONE_KB;
        if (sizeInKb > 1) {
            return new Pair<>(FileSizeUnit.KILOBYTE, sizeInKb);
        }

        return new Pair<>(FileSizeUnit.BYTE, sizeInBytes);
    }
}
