package com.ouchadam.fang.presentation.drawer;

public interface DrawerManipulator {
    void close();
    void setChecked(int position, boolean checked);
    void setOnCloseTitle(String title);
}
