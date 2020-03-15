package com.shark.sonar.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.shark.sonar.utility.DataContainer;

import send.MessageHandler;

public class NetControl {

    public void sendMessage(DataContainer data) {
        new ClientAsyncTask(data).execute(true, false);
    }

    public void sendAuth(DataContainer data) {
        new ClientAsyncTask(data).execute(false, true);
    }

    public boolean isOnline(byte[] ID, MessageHandler handler) {
        DataContainer data = new DataContainer();
        data.setToID(ID);
        data.setHandler(handler);

        isOnlineAsyncTask t = new isOnlineAsyncTask(data);

        try {
            return t.execute().get();
        } catch (Exception e) {
            Log.wtf("Error in isOnlineAsyncTask", e.toString());
        }

        return false;
    }


    //REF https://guides.codepath.com/android/Sending-and-Receiving-Data-with-Sockets
    class ClientAsyncTask extends AsyncTask<Boolean, Void, Void> {

        private String message;
        private MessageHandler client;
        private byte[] toID;

        ClientAsyncTask(DataContainer data) {
            if (!data.isAuth()) {
                this.message = data.getMessage();
                this.toID = data.getToID();
            }
            this.client = data.getHandler();
        }


        @Override
        protected Void doInBackground(Boolean... clientOn) {

            try {
                if (!clientOn[0]) {
                    client.start();
                }

                if (clientOn[1]) {
                    client.auth();
                } else {
                    client.send(message, toID);
                }
            } catch (Exception e) {
                Log.wtf("Network issue", e.toString());
            }


            return null;
        }
    }

    class isOnlineAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private MessageHandler client;
        private byte[] toID;

        isOnlineAsyncTask(DataContainer data) {
            this.toID = data.getToID();
            this.client = data.getHandler();
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {
                return client.isUserOnline(toID);
            } catch (Exception e) {
                Log.wtf("Network issue", e.toString());
            }

            return false;
        }
    }

}