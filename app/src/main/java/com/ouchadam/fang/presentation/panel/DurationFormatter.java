package com.ouchadam.fang.presentation.panel;

import android.content.res.Resources;

import com.ouchadam.fang.FangDuration;
import com.ouchadam.fang.R;

public class DurationFormatter {

    private final Resources resources;

    public DurationFormatter(Resources resources) {
        this.resources = resources;
    }

    public String format(FangDuration duration) {
        int hours = duration.getHours();
        int minutes = duration.getMinutes();
        String hoursString = "";
        if (hours > 0) {
            hoursString = resources.getQuantityString(R.plurals.hour, hours, hours) + " ";
        }
        if (hours > 0 && minutes == 0) {
            return hoursString;
        }
        return hoursString + minutes + " " + "minutes";
    }

}
