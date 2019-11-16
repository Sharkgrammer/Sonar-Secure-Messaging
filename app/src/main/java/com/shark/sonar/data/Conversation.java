package com.shark.sonar.data;

import android.content.Context;

import com.shark.sonar.controller.BridgeDbControl;
import com.shark.sonar.controller.ColourDbControl;
import com.shark.sonar.controller.HistoryDbControl;
import com.shark.sonar.controller.ProfileDbControl;

import java.util.ArrayList;
import java.util.List;

public class Conversation {

    private int conversation_ID;
    private List<History> historyArrayList;
    private Colour colour;
    private Bridge bridge;
    private Profile profile;
    private Context context;

    public Conversation(Context context) {
        this.context = context;
    }

    public int getConversation_ID() {
        return conversation_ID;
    }

    public void setConversation_ID(int conversation_ID) {
        this.conversation_ID = conversation_ID;
    }

    public List<History> getHistoryArrayList() {

        if (historyArrayList == null){
            historyArrayList = new ArrayList<History>();
        }

        return historyArrayList;
    }

    public void setHistoryArrayList(List<History> historyArrayList) {
        this.historyArrayList = historyArrayList;
    }

    public void setHistoryArrayList() {
        HistoryDbControl con = new HistoryDbControl(context);

        historyArrayList = con.selectHistory(conversation_ID);
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        if (colour == null) {
            Colour col = new Colour();
            col.setColour_ID(0);
            colour = col;
        }
        this.colour = colour;
    }

    public void setColour(int Colour_ID) {
        ColourDbControl con = new ColourDbControl(context);

        colour = con.selectSingleColour(Colour_ID);

    }

    public Bridge getBridge() {
        return bridge;
    }

    public void setBridge(Bridge bridge) {
        if (bridge == null) {
            Bridge brid = new Bridge();

            brid.setBridge_ID(0);
            bridge = brid;
        }
        this.bridge = bridge;
    }

    public void setBridge(int Bridge_ID) {
        BridgeDbControl con = new BridgeDbControl(context);

        bridge = con.selectBridge(Bridge_ID);
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfile(Integer Profile_ID) {
        ProfileDbControl con = new ProfileDbControl(context);
        profile = con.selectSingleProfile(Profile_ID);
    }

    public History getLatestMessage() {
        return new History().returnMostRecent();
    }

}