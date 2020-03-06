package com.shark.sonar.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class Message {

    private int image;
    private boolean fromYou;
    private String message;
    private String time;
    private String imageMsg;
    private Bitmap img, cImg;

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
                //REF https://stackoverflow.com/a/4717740/11480852
                img = MediaStore.Images.Media.getBitmap(c.getContentResolver(), Uri.parse(imageMsg));
            } catch (Exception e) {
                return null;
            }

        }

        return img;
    }

    public Bitmap getCompressedImg(Context c) {
        if (cImg == null) {

            try {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                getImg(c).compress(Bitmap.CompressFormat.JPEG, 40, bout);

                byte[] tempBytes = bout.toByteArray();

                cImg = BitmapFactory.decodeByteArray(tempBytes,0,tempBytes.length);
            } catch (Exception e) {
                return null;
            }

        }

        return cImg;
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
