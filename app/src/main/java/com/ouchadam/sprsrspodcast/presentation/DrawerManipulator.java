package com.ouchadam.sprsrspodcast.presentation;

public interface DrawerManipulator {
    void close();
    void setChecked(int position, boolean checked);
    void setOnCloseTitle(String title);
}
