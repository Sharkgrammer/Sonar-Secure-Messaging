package com.shark.sonar.data;

import java.util.ArrayList;

public class Conversation {

    private int conversation_ID;
    private ArrayList<History> historyArrayList;
    private Colour colour;
    private Bridge bridge;
    private Profile user;

    public int getConversation_ID() {
        return conversation_ID;
    }

    public void setConversation_ID(int conversation_ID) {
        this.conversation_ID = conversation_ID;
    }

    public ArrayList<History> getHistoryArrayList() {
        return historyArrayList;
    }

    public void setHistoryArrayList(ArrayList<History> historyArrayList) {
        this.historyArrayList = historyArrayList;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Bridge getBridge() {
        return bridge;
    }

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }
}
