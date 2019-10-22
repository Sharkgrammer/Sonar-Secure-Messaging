package com.shark.sonar.data;

import java.util.ArrayList;

public class Conversation {

    private int conversation_ID;
    private ArrayList<History> historyArrayList;
    private Colour colour;
    private Bridge bridge;
    private Profile profile;

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

    public void setHistory(int History_ID) {
        //TODO fix this pls
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public void setColour(int Colour_ID) {
        //TODO fix this pls
    }

    public Bridge getBridge() {
        return bridge;
    }

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public void setBridge(int Bridge_ID) {
        //TODO fix this pls
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfile(int Profile_ID) {
        //TODO fix this pls
    }

}