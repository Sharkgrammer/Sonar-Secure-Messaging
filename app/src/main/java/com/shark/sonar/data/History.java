package com.shark.sonar.data;

public class History {

    private int History_ID;
    private int Conversation_ID;
    private TextItem messageObj;
    private String end_date;
    private Profile user_from;

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

    public TextItem getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(TextItem messageObj) {
        this.messageObj = messageObj;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Profile getUser_from() {
        return user_from;
    }

    public void setUser_from(Profile user_from) {
        this.user_from = user_from;
    }

    public void setUser_from(int user_from) {
        //TODO fix this pls
    }
}
