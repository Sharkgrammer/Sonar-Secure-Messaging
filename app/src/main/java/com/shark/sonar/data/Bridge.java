package com.shark.sonar.data;

import java.util.ArrayList;

public class Bridge {

    private int Bridge_ID;
    private int conversation_ID;
    private ArrayList<Connection> connectionArrayList;

    public int getBridge_ID() {
        return Bridge_ID;
    }

    public void setBridge_ID(int bridge_ID) {
        this.Bridge_ID = bridge_ID;
    }

    public ArrayList<Connection> getConnectionArrayList() {
        return connectionArrayList;
    }

    public void setConnectionArrayList(ArrayList<Connection> connectionArrayList) {
        this.connectionArrayList = connectionArrayList;
    }

    public int getConversation_ID() {
        return conversation_ID;
    }

    public void setConversation_ID(int conversation_ID) {
        this.conversation_ID = conversation_ID;
    }
}
