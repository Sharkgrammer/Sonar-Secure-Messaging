package com.shark.sonar.data;

import java.sql.Blob;

public class Conversation {

    private int Conversation_ID;
    private Blob User_ID_key;
    private int Bridge_ID;
    private int Colour_ID;

    public int getConversation_ID() {
        return Conversation_ID;
    }

    public void setConversation_ID(int conversation_ID) {
        Conversation_ID = conversation_ID;
    }

    public Blob getUser_ID_key() {
        return User_ID_key;
    }

    public void setUser_ID_key(Blob user_ID_key) {
        User_ID_key = user_ID_key;
    }

    public int getBridge_ID() {
        return Bridge_ID;
    }

    public void setBridge_ID(int bridge_ID) {
        Bridge_ID = bridge_ID;
    }

    public int getColour_ID() {
        return Colour_ID;
    }

    public void setColour_ID(int colour_ID) {
        Colour_ID = colour_ID;
    }
}
