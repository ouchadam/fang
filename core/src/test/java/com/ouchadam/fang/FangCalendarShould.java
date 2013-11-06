package com.ouchadam.fang;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class FangCalendarShould {


    //    <pubDate>Sat, 27 Jul 2013 06:06:39 +0100</pubDate>
    //    <pubDate>Tue, 23 Jul 2013 10:07:23 -0400</pubDate>
    //    <pubDate>Tue, 06 Aug 2013 11:12:00 PST</pubDate>


    @Test
    public void parse_handle_dates() throws Exception {
        String date = "Sat, 27 Jul 2013 06:06:39 +0100";

        FangCalendar fangCalendar = new FangCalendar(date);

        assertThat(fangCalendar.get(NovodaCalendar.DateInteger.DAY_IN_MONTH)).isEqualTo(27);
    }

    @Test
    public void parse_handle_pst_dates() throws Exception {
        String date = "Tue, 06 Aug 2013 11:12:00 PST";

        FangCalendar fangCalendar = new FangCalendar(date);

        assertThat(fangCalendar.get(NovodaCalendar.DateInteger.DAY_IN_MONTH)).isEqualTo(6);
    }
}
