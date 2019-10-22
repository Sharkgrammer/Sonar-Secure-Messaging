package com.shark.sonar.controller;

import android.content.Context;

public class ConvoDbControl extends DbControl {

    public ConvoDbControl(Context c){
        super(c);
    }

    public void test(){
        db.execSQL("");
    }

}
