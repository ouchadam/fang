package com.ouchadam.fang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FangDuration extends NovodaCalendar {

    private static final SimpleDateFormat HMSFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat MSFormat = new SimpleDateFormat("mm:ss");
    private static final SimpleDateFormat SFormat = new SimpleDateFormat("ssssss");

    private final String rawTime;

    public FangDuration(String time) {
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
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == ':') {
                colonCount++;
            }
        }
        return colonCount;
    }

    public String formatted() {
        if (hasHours()) {
            return get(DateInteger.HOUR_24) + " " + get(DateInteger.MINUTE) + "mins";
        }
        return "duration";
    }

    private boolean hasHours() {
        return get(DateInteger.HOUR_24) != 0;
    }

    public String getRaw() {
        return rawTime;
    }
}
