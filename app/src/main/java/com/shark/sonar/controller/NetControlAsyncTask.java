package com.shark.sonar.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.shark.sonar.utility.DataContainer;

import send.MessageHandler;

//REF https://guides.codepath.com/android/Sending-and-Receiving-Data-with-Sockets
public class NetControlAsyncTask extends AsyncTask<Boolean, Void, Void> {

    private String message;
    private MessageHandler client;
    private byte[] toID;

    public NetControlAsyncTask(DataContainer data) {
        if (!data.isAuth()){
            this.message = data.getMessage();
            this.toID = data.getToID();
        }
        this.client = data.getHandler();
    }


    @Override
    protected Void doInBackground(Boolean... clientOn) {

        try{
            if (!clientOn[0]) {
                client.start();
            }

            if (clientOn[1]) {
                client.auth();
            } else {
                client.send(message, toID);
            }
        }catch(Exception e){
            Log.wtf("Network issue", e.toString());
        }


        return null;
    }
}