package com.shark.sonar.data;

public class History {

    private int History_ID;
    private int Conversation_ID;
    private String Mesasge;
    private String Time;
    private String End_date;

    public int getHistory_ID() {
        return History_ID;
    }

    public void setHistory_ID(int history_ID) {
        History_ID = history_ID;
    }

    public int getConversation_ID() {
        return Conversation_ID;
    }

    public void setConversation_ID(int conversation_ID) {
        Conversation_ID = conversation_ID;
    }

    public String getMesasge() {
        return Mesasge;
    }

    public void setMesasge(String mesasge) {
        Mesasge = mesasge;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }
}
