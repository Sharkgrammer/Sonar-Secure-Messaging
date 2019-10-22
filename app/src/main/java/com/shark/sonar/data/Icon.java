package com.shark.sonar.data;

import android.graphics.Bitmap;

public class Icon {

    private int Icon_ID;
    private Bitmap Icon;
    private String Icon_Loc;

    public int getIcon_ID() {
        return Icon_ID;
    }

    public void setIcon_ID(int icon_ID) {
        this.Icon_ID = icon_ID;
    }

    public Bitmap getIcon() {
        return Icon;
    }

    public void setIcon(Bitmap icon) {
        Icon = icon;
    }

    public String getIcon_Loc() {
        return Icon_Loc;
    }

    public void setIcon_Loc(String icon_Loc) {
        Icon_Loc = icon_Loc;
    }
}
