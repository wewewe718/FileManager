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
        // Image
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
        typeMap.put("jpe", DirectoryItemType.IMAGE);
        typeMap.put("pcx", DirectoryItemType.IMAGE);
        typeMap.put("png", DirectoryItemType.IMAGE);
        typeMap.put("svg", DirectoryItemType.IMAGE);
        typeMap.put("svgz", DirectoryItemType.IMAGE);
        typeMap.put("tiff", DirectoryItemType.IMAGE);
        typeMap.put("tif", DirectoryItemType.IMAGE);
        typeMap.put("wbmp", DirectoryItemType.IMAGE);
        typeMap.put("webp", DirectoryItemType.IMAGE);
        typeMap.put("dng", DirectoryItemType.IMAGE);
        typeMap.put("cr2", DirectoryItemType.IMAGE);
        typeMap.put("ras", DirectoryItemType.IMAGE);
        typeMap.put("art", DirectoryItemType.IMAGE);
        typeMap.put("jng", DirectoryItemType.IMAGE);
        typeMap.put("nef", DirectoryItemType.IMAGE);
        typeMap.put("nrw", DirectoryItemType.IMAGE);
        typeMap.put("orf", DirectoryItemType.IMAGE);
        typeMap.put("rw2", DirectoryItemType.IMAGE);
        typeMap.put("pef", DirectoryItemType.IMAGE);
        typeMap.put("psd", DirectoryItemType.IMAGE);
        typeMap.put("pnm", DirectoryItemType.IMAGE);
        typeMap.put("pbm", DirectoryItemType.IMAGE);
        typeMap.put("pgm", DirectoryItemType.IMAGE);
        typeMap.put("ppm", DirectoryItemType.IMAGE);
        typeMap.put("srw", DirectoryItemType.IMAGE);
        typeMap.put("arw", DirectoryItemType.IMAGE);
        typeMap.put("rgb", DirectoryItemType.IMAGE);
        typeMap.put("xbm", DirectoryItemType.IMAGE);
        typeMap.put("xpm", DirectoryItemType.IMAGE);
        typeMap.put("xwd", DirectoryItemType.IMAGE);
        
        // Audio
        typeMap.put("wav", DirectoryItemType.AUDIO);
        typeMap.put("aac", DirectoryItemType.AUDIO);
        typeMap.put("mp3", DirectoryItemType.AUDIO);
        typeMap.put("adp", DirectoryItemType.AUDIO);
        typeMap.put("au", DirectoryItemType.AUDIO);
        typeMap.put("snd", DirectoryItemType.AUDIO);
        typeMap.put("m2a", DirectoryItemType.AUDIO);
        typeMap.put("m3a", DirectoryItemType.AUDIO);
        typeMap.put("oga", DirectoryItemType.AUDIO);
        typeMap.put("ogg", DirectoryItemType.AUDIO);
        typeMap.put("spx", DirectoryItemType.AUDIO);
        typeMap.put("mka", DirectoryItemType.AUDIO);
        typeMap.put("amr", DirectoryItemType.AUDIO);
        typeMap.put("awb", DirectoryItemType.AUDIO);
        typeMap.put("flac", DirectoryItemType.AUDIO);
        typeMap.put("mpga", DirectoryItemType.AUDIO);
        typeMap.put("mpega", DirectoryItemType.AUDIO);
        typeMap.put("mp2", DirectoryItemType.AUDIO);
        typeMap.put("m4a", DirectoryItemType.AUDIO);
        typeMap.put("aif", DirectoryItemType.AUDIO);
        typeMap.put("aiff", DirectoryItemType.AUDIO);
        typeMap.put("aifc", DirectoryItemType.AUDIO);
        typeMap.put("gsm", DirectoryItemType.AUDIO);
        typeMap.put("m3u", DirectoryItemType.AUDIO);
        typeMap.put("wma", DirectoryItemType.AUDIO);
        typeMap.put("wax", DirectoryItemType.AUDIO);
        typeMap.put("ra", DirectoryItemType.AUDIO);
        typeMap.put("rm", DirectoryItemType.AUDIO);
        typeMap.put("ram", DirectoryItemType.AUDIO);
        typeMap.put("pls", DirectoryItemType.AUDIO);
        typeMap.put("sd2", DirectoryItemType.AUDIO);
        
        // Video
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
        typeMap.put("3gpp", DirectoryItemType.VIDEO);
        typeMap.put("3gp", DirectoryItemType.VIDEO);
        typeMap.put("3gpp2", DirectoryItemType.VIDEO);
        typeMap.put("3g2", DirectoryItemType.VIDEO);
        typeMap.put("avi", DirectoryItemType.VIDEO);
        typeMap.put("dl", DirectoryItemType.VIDEO);
        typeMap.put("dif", DirectoryItemType.VIDEO);
        typeMap.put("dv", DirectoryItemType.VIDEO);
        typeMap.put("fli", DirectoryItemType.VIDEO);
        typeMap.put("m4v", DirectoryItemType.VIDEO);
        typeMap.put("ts", DirectoryItemType.VIDEO);
        typeMap.put("mpeg", DirectoryItemType.VIDEO);
        typeMap.put("mpg", DirectoryItemType.VIDEO);
        typeMap.put("mpe", DirectoryItemType.VIDEO);
        typeMap.put("vob", DirectoryItemType.VIDEO);
        typeMap.put("qt", DirectoryItemType.VIDEO);
        typeMap.put("mov", DirectoryItemType.VIDEO);
        typeMap.put("mxu", DirectoryItemType.VIDEO);
        typeMap.put("webm", DirectoryItemType.VIDEO);
        typeMap.put("lsf", DirectoryItemType.VIDEO);
        typeMap.put("lsx", DirectoryItemType.VIDEO);
        typeMap.put("mng", DirectoryItemType.VIDEO);
        typeMap.put("asf", DirectoryItemType.VIDEO);
        typeMap.put("asx", DirectoryItemType.VIDEO);
        typeMap.put("wm", DirectoryItemType.VIDEO);
        typeMap.put("wmv", DirectoryItemType.VIDEO);
        typeMap.put("wmx", DirectoryItemType.VIDEO);
        typeMap.put("wvx", DirectoryItemType.VIDEO);
        typeMap.put("movie", DirectoryItemType.VIDEO);
        typeMap.put("wrf", DirectoryItemType.VIDEO);
        
        // Archive
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

        // Text
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
