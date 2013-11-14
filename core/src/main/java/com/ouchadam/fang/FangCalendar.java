package com.ouchadam.fang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FangCalendar extends NovodaCalendar {

    public FangCalendar(String time) {
        super(time);
    }

    public FangCalendar(long time) {
        super(time);
    }

    @Override
    protected DateFormat getDateFormat(String date) {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
    }

    //    <pubDate>Tue, 06 Aug 2013 11:12:00 PST</pubDate>

    public String getTimeAgo() {
        long hoursDiff = getDateDiff(System.currentTimeMillis(), getTimeInMillis(), TimeUnit.HOURS);
        long days = hoursDiff / 24;
        if (days > 1) {
            return days + " days ago";
        } else {
            return hoursDiff + " hours ago";
        }
    }

    public long getHoursDifference() {
        return getDateDiff(System.currentTimeMillis(), getTimeInMillis(), TimeUnit.HOURS);
    }

    public static long getDateDiff(long newer, long older, TimeUnit timeUnit) {
        long diffInMillies = newer - older;
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
