package com.shark.sonar.data;

import java.sql.Blob;

public class Profile {

    private int Profile_ID;
    private String Name;
    private int Icon_ID;
    private Blob user_key_public;
    private Blob user_key_private;
    private Blob user_ID_key;

    public int getProfile_ID() {
        return Profile_ID;
    }

    public void setProfile_ID(int profile_ID) {
        Profile_ID = profile_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIcon_ID() {
        return Icon_ID;
    }

    public void setIcon_ID(int icon_ID) {
        Icon_ID = icon_ID;
    }

    public Blob getUser_key_public() {
        return user_key_public;
    }

    public void setUser_key_public(Blob user_key_public) {
        this.user_key_public = user_key_public;
    }

    public Blob getUser_key_private() {
        return user_key_private;
    }

    public void setUser_key_private(Blob user_key_private) {
        this.user_key_private = user_key_private;
    }

    public Blob getUser_ID_key() {
        return user_ID_key;
    }

    public void setUser_ID_key(Blob user_ID_key) {
        this.user_ID_key = user_ID_key;
    }
}