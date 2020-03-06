package com.shark.sonar.data;

import android.content.Context;

import com.shark.sonar.controller.IconDbControl;

public class Profile {

    private int Profile_ID;
    private String Name;
    private Icon Icon;
    private byte[] user_key_public;
    private byte[] user_key_private;
    private byte[] user_ID_key;
    private Context context;

    public Profile(){}

    public Profile(Context context){
        this.context =  context;
    }

    public Profile(Integer ID, String Name, Icon icon, byte[] publicKey, byte[] privateKey, byte[] userKey){
        if (ID != null){
            this.Profile_ID = ID;
        }

        this.Name = Name;
        this.Icon = icon;
        this.user_key_public = publicKey;
        this.user_key_private = privateKey;
        this.user_ID_key = userKey;
    }

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

    public Icon getIcon() {
        return Icon;
    }

    public void setIcon(Icon Icon) {
        this.Icon = Icon;
    }

    public void setIcon(int Icon_ID) {
        IconDbControl con = new IconDbControl(context);

        Icon = con.selectSingleIcon(Icon_ID);

        con.destroy();
    }

    public byte[] getUser_key_public() {
        return user_key_public;
    }

    public void setUser_key_public(byte[] user_key_public) {
        this.user_key_public = user_key_public;
    }

    public byte[] getUser_key_private() {
        return user_key_private;
    }

    public void setUser_key_private(byte[] user_key_private) {
        this.user_key_private = user_key_private;
    }

    public byte[] getUser_ID_key() {
        return user_ID_key;
    }

    public void setUser_ID_key(byte[] user_ID_key) {
        this.user_ID_key = user_ID_key;
    }
}