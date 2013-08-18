package com.ouchadam.fang;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FangCalendar extends NovodaCalendar {

    public FangCalendar(String time) {
        super(time);
    }

    @Override
    protected DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzzz");
    }

    //    <pubDate>Sat, 27 Jul 2013 06:06:39 +0100</pubDate>
    //    <pubDate>Tue, 23 Jul 2013 10:07:23 -0400</pubDate>

}
