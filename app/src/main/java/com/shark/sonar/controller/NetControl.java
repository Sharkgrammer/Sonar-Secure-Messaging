package com.shark.sonar.controller;

import android.DataContainer;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.scottyab.aescrypt.AESCrypt;
import com.shark.sonar.R;

import java.net.Socket;
import java.security.GeneralSecurityException;

import send.MessageHandler;
import util.DataHolder;
import util.ResultHandler;

public class NetControl implements ResultHandler {

    private final Activity act;
    private MessageHandler client;
    private boolean clientOn = false;

    public NetControl(Activity act, DataHolder server, String ID) {
        this.act = act;
        client = new MessageHandler(server, this, ID.getBytes());
    }

    public void stop(){
        client.stop();
    }

    public void sendMessage(String message, byte[] to){

        DataContainer data = new DataContainer();

        String password = "password";
        try {
            String encryptedMsg = AESCrypt.encrypt(password, message);
            data.setMessage(encryptedMsg);
        }catch (GeneralSecurityException e){
            //handle error
        }
        data.setToID(to);
        //TODO set auth as false defaultly
        data.setAuth(false);
        data.setHandler(client);

        if (!clientOn){
            //clientOn = true;

            //TODO there is a worry that client.start is called each time the thing runs, meaning that multiple threads could return 1 message
            //TODO this needs to be fixed later
        }

        new NetControlAsyncTask(data).execute(clientOn, false);
    }

    public void sendAuthMessage(){
        DataContainer data = new DataContainer();

        data.setAuth(true);
        data.setHandler(client);

        new NetControlAsyncTask(data).execute(clientOn, true);
    }

    @Override
    public void messageReceived(String message, Socket socket, DataHolder dataHolder) {

        String password = "password";
        try {
            final String messageAfterDecrypt = AESCrypt.decrypt(password, message);

            System.out.println("Message from server: " + message + " : " + messageAfterDecrypt);

            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TextView response = act.findViewById(R.id.lblResponse);

                    response.setText(messageAfterDecrypt);
                }
            });

        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
        }

    }
}