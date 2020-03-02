package com.shark.sonar.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

public class Message {

    private int image;
    private boolean fromYou;
    private String message;
    private String time;
    private String imageMsg;
    private Bitmap img;

    public Message(int map, boolean fromYou, String message, String time, String imageMsg) {
        this.image = map;
        this.setFromYou(fromYou);
        this.message = message;
        this.time = time;
        this.imageMsg = imageMsg;
        this.img = null;
    }

    public Message(int map, boolean fromYou, String message, String time) {
        this.image = map;
        this.setFromYou(fromYou);

        this.imageMsg = "";
        if (message.length() > 5) {
            if (message.substring(0, 5).equals("img::")) {
                imageMsg = message.split("img::")[1];
                message = "";
            }
        }

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

    public Bitmap getImg(Context c) {
        if (img == null) {

            try {
                img = MediaStore.Images.Media.getBitmap(c.getContentResolver(), Uri.parse(imageMsg));
            } catch (Exception e) {
                return null;
            }

        }

        return img;

    }

    public void setFromYou(boolean fromYou) {
        this.fromYou = fromYou;
    }

    public String getImageMsg() {
        return imageMsg;
    }

    public void setImageMsg(String imageMsg) {
        this.imageMsg = imageMsg;
    }
}
