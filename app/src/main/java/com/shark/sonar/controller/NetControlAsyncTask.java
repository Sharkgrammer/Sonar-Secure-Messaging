package com.shark.sonar.controller;

import android.DataContainer;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.shark.sonar.R;

import java.net.Socket;

import send.MessageHandler;

//REF https://guides.codepath.com/android/Sending-and-Receiving-Data-with-Sockets
public class NetControlAsyncTask extends AsyncTask<Boolean, Void, Void> {

    private String message;
    private MessageHandler client;
    private byte[] toID;

    NetControlAsyncTask(DataContainer data) {
        if (!data.isAuth()){
            this.message = data.getMessage();
            this.toID = data.getToID();
        }
        this.client = data.getHandler();
    }


    @Override
    protected Void doInBackground(Boolean... clientOn) {

        if (!clientOn[0]) {
            client.start();
        }

        if (clientOn[1]) {
            client.auth();
        } else {
            client.send(message, toID);
        }

        return null;
    }
}