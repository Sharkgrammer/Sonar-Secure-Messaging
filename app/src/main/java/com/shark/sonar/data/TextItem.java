package com.shark.sonar.data;

public class TextItem {

    private boolean toSide;
    private String text;
    private String time;

    public boolean isToSide() {
        return toSide;
    }

    public void setToSide(boolean toSide) {
        this.toSide = toSide;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
