package com.example.filemanager.util;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat( "dd-MM-yyyy", Locale.ENGLISH);

    @NonNull
    public static String formatDate(@NonNull Date date) {
        return DATE_FORMATTER.format(date);
    }
}
