package com.shark.sonar.data;

import java.sql.Blob;

public class Connection {

    private int Connection_ID;
    private int Bridge_ID;
    private String Server_IP;
    private Blob key;
    private int position;

    public int getConnection_ID() {
        return Connection_ID;
    }

    public void setConnection_ID(int connection_ID) {
        Connection_ID = connection_ID;
    }

    public String getServer_IP() {
        return Server_IP;
    }

    public void setServer_IP(String server_IP) {
        Server_IP = server_IP;
    }

    public Blob getKey() {
        return key;
    }

    public void setKey(Blob key) {
        this.key = key;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBridge_ID() {
        return Bridge_ID;
    }

    public void setBridge_ID(int bridge_ID) {
        Bridge_ID = bridge_ID;
    }
}
