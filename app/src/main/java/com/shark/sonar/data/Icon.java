package com.shark.sonar.data;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Icon {

    private int Icon_ID;
    private Drawable Icon;

    public Icon(){
    }

    public Icon(int Icon, Context context){
        this.Icon_ID = Icon;
        this.Icon = context.getResources().getDrawable(Icon, null);
    }

    public int getIcon_ID() {
        return Icon_ID;
    }

    public Drawable getIcon() {
        return Icon;
    }

}
