package com.shark.sonar.data;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class MainMessage {

    private int image;
    private String person;
    private String message;
    private String time;

    public MainMessage(int map, String person, String message, String time){
        this.image = map;
        this.person = person;
        this.message = message;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        person = person;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
