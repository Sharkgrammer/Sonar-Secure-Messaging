package com.shark.sonar.utility;

import send.MessageHandler;

public class DataContainer {

    private String message;
    private byte[] toID;
    private MessageHandler handler;
    private boolean isAuth;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getToID() {
        return toID;
    }

    public void setToID(byte[] toID) {
        this.toID = toID;
    }

    public MessageHandler getHandler() {
        return handler;
    }

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
