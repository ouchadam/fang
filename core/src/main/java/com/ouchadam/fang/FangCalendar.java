package com.ouchadam.fang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FangCalendar extends NovodaCalendar {

    public FangCalendar(String time) {
        super(time);
    }

    @Override
    protected DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.UK);
    }

    //    <pubDate>Tue, 06 Aug 2013 11:12:00 PST</pubDate>


}
