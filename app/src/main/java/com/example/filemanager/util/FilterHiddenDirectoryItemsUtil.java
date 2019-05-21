package com.example.filemanager.util;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;

import java.util.ArrayList;
import java.util.List;

public class FilterHiddenDirectoryItemsUtil {
    @NonNull
    public static List<DirectoryItem> filterHiddenFiles(@NonNull List<DirectoryItem> items, boolean showHidden) {
        List<DirectoryItem> result = new ArrayList<>();
        for (DirectoryItem item : items) {
            if (item.isHidden() && !showHidden) {
                continue;
            }
            result.add(item);
        }
        return result;
    }
}
