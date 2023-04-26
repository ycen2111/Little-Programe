package com.edinburgh.ewireless.Class.Trajectory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A utility class for converting UNIX timestamps to formatted strings representing the time.
 */
public class timeStampToStringTime {

    /**
     * Converts a UNIX timestamp to a formatted string representing the time in the format yyyy/MM/dd HH:mm:ss.
     *
     * @param timeStamp The UNIX timestamp to be converted.
     * @return A formatted string representing the time.
     */
    public static String yyyymmddhhmmss(long timeStamp){
        SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        return time.format(new Date(timeStamp));
    }

    /**
     * Converts a UNIX timestamp to a formatted string representing the time in the format mm:ss.SSS.
     *
     * @param timeStamp The UNIX timestamp to be converted.
     * @return A formatted string representing the time.
     */
    public static String mmssmmm(long timeStamp){
        SimpleDateFormat time = new SimpleDateFormat("mm:ss.SSS", Locale.getDefault());
        return time.format(new Date(timeStamp));
    }
}
