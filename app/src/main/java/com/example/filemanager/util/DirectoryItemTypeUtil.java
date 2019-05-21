package com.example.filemanager.util;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.example.filemanager.model.DirectoryItemType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DirectoryItemTypeUtil {
    private static Map<String, DirectoryItemType> typeMap = new HashMap<>();

    static {
        typeMap.put("jpeg", DirectoryItemType.IMAGE);
        typeMap.put("jpg", DirectoryItemType.IMAGE);
        typeMap.put("bmp", DirectoryItemType.IMAGE);
        typeMap.put("gif", DirectoryItemType.IMAGE);
        typeMap.put("cgm", DirectoryItemType.IMAGE);
        typeMap.put("btif", DirectoryItemType.IMAGE);
        typeMap.put("dwg", DirectoryItemType.IMAGE);
        typeMap.put("dxf", DirectoryItemType.IMAGE);
        typeMap.put("fbs", DirectoryItemType.IMAGE);
        typeMap.put("fpx", DirectoryItemType.IMAGE);
        typeMap.put("fst", DirectoryItemType.IMAGE);
        typeMap.put("mdi", DirectoryItemType.IMAGE);
        typeMap.put("npx", DirectoryItemType.IMAGE);
        typeMap.put("xif", DirectoryItemType.IMAGE);
        typeMap.put("pct", DirectoryItemType.IMAGE);
        typeMap.put("pic", DirectoryItemType.IMAGE);

        typeMap.put("wav", DirectoryItemType.AUDIO);
        typeMap.put("aac", DirectoryItemType.AUDIO);
        typeMap.put("mp3", DirectoryItemType.AUDIO);
        typeMap.put("adp", DirectoryItemType.AUDIO);
        typeMap.put("au", DirectoryItemType.AUDIO);
        typeMap.put("snd", DirectoryItemType.AUDIO);
        typeMap.put("m2a", DirectoryItemType.AUDIO);
        typeMap.put("m3a", DirectoryItemType.AUDIO);
        typeMap.put("oga", DirectoryItemType.AUDIO);
        typeMap.put("spx", DirectoryItemType.AUDIO);
        typeMap.put("mka", DirectoryItemType.AUDIO);

        typeMap.put("jpgv", DirectoryItemType.VIDEO);
        typeMap.put("jpgm", DirectoryItemType.VIDEO);
        typeMap.put("jpm", DirectoryItemType.VIDEO);
        typeMap.put("mj2", DirectoryItemType.VIDEO);
        typeMap.put("mjp2", DirectoryItemType.VIDEO);
        typeMap.put("mpa", DirectoryItemType.VIDEO);
        typeMap.put("ogv", DirectoryItemType.VIDEO);
        typeMap.put("flv", DirectoryItemType.VIDEO);
        typeMap.put("mkv", DirectoryItemType.VIDEO);
        typeMap.put("mp4", DirectoryItemType.VIDEO);

        typeMap.put("ace", DirectoryItemType.ARCHIVE);
        typeMap.put("bz", DirectoryItemType.ARCHIVE);
        typeMap.put("bz2", DirectoryItemType.ARCHIVE);
        typeMap.put("cab", DirectoryItemType.ARCHIVE);
        typeMap.put("gz", DirectoryItemType.ARCHIVE);
        typeMap.put("lrf", DirectoryItemType.ARCHIVE);
        typeMap.put("jar", DirectoryItemType.ARCHIVE);
        typeMap.put("xz", DirectoryItemType.ARCHIVE);
        typeMap.put("Z", DirectoryItemType.ARCHIVE);
        typeMap.put("zip", DirectoryItemType.ARCHIVE);

        typeMap.put("asm", DirectoryItemType.TEXT);
        typeMap.put("json", DirectoryItemType.TEXT);
        typeMap.put("js", DirectoryItemType.TEXT);
        typeMap.put("def", DirectoryItemType.TEXT);
        typeMap.put("in", DirectoryItemType.TEXT);
        typeMap.put("rc", DirectoryItemType.TEXT);
        typeMap.put("list", DirectoryItemType.TEXT);
        typeMap.put("log", DirectoryItemType.TEXT);
        typeMap.put("pl", DirectoryItemType.TEXT);
        typeMap.put("prop", DirectoryItemType.TEXT);
        typeMap.put("properties", DirectoryItemType.TEXT);
        typeMap.put("ini", DirectoryItemType.TEXT);
        typeMap.put("md", DirectoryItemType.TEXT);
        typeMap.put("epub", DirectoryItemType.TEXT);
        typeMap.put("ibooks", DirectoryItemType.TEXT);
        typeMap.put("pdf", DirectoryItemType.TEXT);
        typeMap.put("doc", DirectoryItemType.TEXT);
        typeMap.put("docx", DirectoryItemType.TEXT);
        typeMap.put("txt", DirectoryItemType.TEXT);
        typeMap.put("htm", DirectoryItemType.TEXT);
        typeMap.put("html", DirectoryItemType.TEXT);
        typeMap.put("xml", DirectoryItemType.TEXT);
    }

    @NonNull
    public static DirectoryItemType getDirectoryItemType(@NonNull File file) {
        if (file.isDirectory()) {
            return DirectoryItemType.DIRECTORY;
        }

        String extension = getFileExtension(file);
        return getDirectoryItemType(extension);
    }

    @NonNull
    private static String getFileExtension(@NonNull File file) {
        return MimeTypeMap.getFileExtensionFromUrl(file.getPath());
    }

    @NonNull
    private static DirectoryItemType getDirectoryItemType(@NonNull String extension) {
        DirectoryItemType type = typeMap.get(extension);
        return (type != null) ? type : DirectoryItemType.OTHER;
    }
}
