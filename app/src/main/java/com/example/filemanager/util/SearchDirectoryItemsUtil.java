package com.example.filemanager.util;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;

import java.util.ArrayList;
import java.util.List;

public class SearchDirectoryItemsUtil {

    @NonNull
    public static List<DirectoryItem> searchDirectoryItems(@NonNull List<DirectoryItem> items, @NonNull String query) {
        if (query.isEmpty()) {
            return items;
        }

        String queryLowerCase = query.toLowerCase();
        List<DirectoryItem> result = new ArrayList<>();

        for (DirectoryItem item : items) {
            if (item.getName().toLowerCase().contains(queryLowerCase)) {
                result.add(item);
            }
        }

        return result;
    }
}
