package com.ouchadam.fang.presentation.item;

import android.app.Activity;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.presentation.ActionBarManipulator;

class ActionBarTitleSetter implements ActivityCallback {

    private ActionBarManipulator actionBarManipulator;

    @Override
    public void onAttach(Activity activity) {
        actionBarManipulator = Classes.from(activity);
    }

    public void set(String title) {
        actionBarManipulator.setTitle(title);
    }

}
