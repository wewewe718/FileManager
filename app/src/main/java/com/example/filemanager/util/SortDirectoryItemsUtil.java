package com.example.filemanager.util;

import android.support.annotation.NonNull;

import com.example.filemanager.model.DirectoryItem;
import com.example.filemanager.model.SortType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortDirectoryItemsUtil {
    private static Comparator<DirectoryItem> NAME_COMPARATOR = new NameComparator();
    private static Comparator<DirectoryItem> DATE_COMPARATOR = new DateComparator();
    private static Comparator<DirectoryItem> TYPE_COMPARATOR = new TypeComparator();
    private static Comparator<DirectoryItem> SIZE_COMPARATOR = new SizeComparator();


    @NonNull
    public static List<DirectoryItem> sort(@NonNull List<DirectoryItem> items, @NonNull SortType sortType) {
        Comparator<DirectoryItem> comparator = getComparator(sortType);
        Collections.sort(items, comparator);
        return items;
    }

    @NonNull
    private static Comparator<DirectoryItem> getComparator(@NonNull SortType sortType) {
        switch (sortType) {
            case NAME: return NAME_COMPARATOR;
            case DATE: return DATE_COMPARATOR;
            case TYPE: return TYPE_COMPARATOR;
            case SIZE: return SIZE_COMPARATOR;
            default: throw new IllegalArgumentException();
        }
    }


    private static class NameComparator implements Comparator<DirectoryItem> {
        @Override
        public int compare(DirectoryItem o1, DirectoryItem o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private static class DateComparator implements Comparator<DirectoryItem> {
        @Override
        public int compare(DirectoryItem o1, DirectoryItem o2) {
            return o1.getLastModificationDate().compareTo(o2.getLastModificationDate());
        }
    }

    private static class TypeComparator implements Comparator<DirectoryItem> {
        @Override
        public int compare(DirectoryItem o1, DirectoryItem o2) {
            return o1.getType().toInt() - o2.getType().toInt();
        }
    }

    public static class SizeComparator implements Comparator<DirectoryItem> {
        @Override
        public int compare(DirectoryItem o1, DirectoryItem o2) {
            return Long.compare(o1.getFileSizeInBytes(), o2.getFileSizeInBytes());
        }
    }
}
