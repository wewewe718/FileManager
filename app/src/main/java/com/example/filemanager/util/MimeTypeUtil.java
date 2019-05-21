package com.example.filemanager.util;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;

public class MimeTypeUtil {
    @NonNull
    public static String getMimeType(@NonNull DirectoryItem item) {
        switch (item.getType()) {
            case IMAGE: return "image/*";
            case AUDIO: return "audio/*";
            case VIDEO: return "video/*";
            case TEXT: return "text/*";
            default: return "application/*";
        }
    }
}
