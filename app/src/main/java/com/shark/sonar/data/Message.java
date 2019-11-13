package com.shark.sonar.data;

public class Message {

    private int image;
    private boolean fromYou;
    private String message;
    private String time;

    public Message(int map, boolean fromYou, String message, String time){
        this.image = map;
        this.setFromYou(fromYou);
        this.message = message;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFromYou() {
        return fromYou;
    }

    public void setFromYou(boolean fromYou) {
        this.fromYou = fromYou;
    }
}
