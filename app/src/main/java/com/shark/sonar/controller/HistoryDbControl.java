package com.shark.sonar.controller;

import android.content.Context;

public class HistoryDbControl extends DbControl {

    public HistoryDbControl(Context c) {
        super(c);
    }

    public void test(){
        db.execSQL("");
    }

}
