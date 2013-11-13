package com.ouchadam.fang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FangDuration extends NovodaCalendar {

    private static final SimpleDateFormat HMSFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat MSFormat = new SimpleDateFormat("mm:ss");
    private static final SimpleDateFormat SFormat = new SimpleDateFormat("ssssss");

    private final String rawTime;

    public static FangDuration from(String duration) {
        return duration == null ? missing() : new FangDuration(duration);
    }

    private FangDuration(String time) {
        super(time);
        rawTime = time;
    }

    @Override
    protected DateFormat getDateFormat(String date) {
        int colonCount = getColonCount(date);
        if (colonCount == 0) {
            return SFormat;
        } else {
            return colonCount > 1 ? HMSFormat : MSFormat;
        }
    }

    private int getColonCount(String date) {
        int colonCount = 0;
        if (date != null) {
            for (int i = 0; i < date.length(); i++) {
                if (date.charAt(i) == ':') {
                    colonCount++;
                }
            }
        }
        return colonCount;
    }

    public String getRaw() {
        return rawTime;
    }

    public int getHours() {
        return get(DateInteger.HOUR_24);
    }

    public int getMinutes() {
        return get(DateInteger.MINUTE);
    }

    public static FangDuration missing() {
        return new FangDuration("10:00");
    }
}
