package com.shark.sonar.data;

import android.content.Context;

import com.shark.sonar.controller.BridgeDbControl;
import com.shark.sonar.controller.ColourDbControl;
import com.shark.sonar.controller.ConvoDbControl;
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

        HistoryDbControl historyDbControl = new HistoryDbControl(context);
        historyArrayList = historyDbControl.selectHistory(conversation_ID);
        historyDbControl.destroy();

        return historyArrayList;
    }

    public void setHistoryArrayList(List<History> historyArrayList) {
        this.historyArrayList = historyArrayList;
    }

    public void setHistoryArrayList() {
        HistoryDbControl con = new HistoryDbControl(context);
        historyArrayList = con.selectHistory(conversation_ID);
        con.destroy();
    }

    public Colour getColour() {
        if (colour == null){
            ColourDbControl db = new ColourDbControl(context);
            colour = db.selectSingleColour(1);
            db.destroy();
        }
        return colour;
    }

    public void setColour(Colour colour) {
        if (colour == null){
            ColourDbControl db = new ColourDbControl(context);
            colour = db.selectSingleColour(1);
            db.destroy();
        }
        this.colour = colour;
    }

    public void setColour(int Colour_ID) {
        ColourDbControl con = new ColourDbControl(context);
        colour = con.selectSingleColour(Colour_ID);
        con.destroy();

    }

    public void updateColour(){
        ConvoDbControl db = new ConvoDbControl(context);
        db.updateConvo(this.conversation_ID, this);
        db.destroy();
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
        con.destroy();
    }

    public Profile getProfile() {
        return profile;
    }

    public void refreshProfile(){
        ProfileDbControl con = new ProfileDbControl(context);
        profile = con.selectSingleProfile(profile.getProfile_ID());
        con.destroy();
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setProfile(Integer Profile_ID) {
        ProfileDbControl con = new ProfileDbControl(context);
        profile = con.selectSingleProfile(Profile_ID);
        con.destroy();
    }

    public History getLatestMessage() {
        if (historyArrayList == null) {
            getHistoryArrayList();
        }

        return historyArrayList.get(historyArrayList.size() - 1);
    }

    public byte[] getPublicKey(){
        return profile.getUser_key_public();
    }

}