package com.ouchadam.fang.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.ouchadam.fang.R;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

    private boolean isChecked;

    public CheckableRelativeLayout(Context context) {
        super(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        setBackground(checked);
    }

    private void setBackground(boolean checked) {
        if (checked) {
            setBackgroundColor(getResources().getColor(R.color.holo_blue));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(getResources().getDrawable(R.drawable.card_background));
            } else {
                setBackgroundDrawable(getResources().getDrawable(R.drawable.card_background));
            }
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
