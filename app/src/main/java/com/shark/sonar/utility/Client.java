package com.shark.sonar.utility;

import android.DataContainer;
import android.util.Log;

import com.shark.sonar.activity.MainActivity;
import com.shark.sonar.activity.MessageActivity;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.NetControlAsyncTask;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MainAdapter;
import com.shark.sonar.recycler.MessageAdapter;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import crypto.CryptManager;
import send.MessageHandler;
import util.Base64Util;
import util.DataHolder;
import util.ResultHandler;
import util.UserHolder;

public class Client implements ResultHandler {

    private MessageHandler client;
    private DataHolder dataHolder;
    private MainActivity mainActivity;
    private MessageActivity currentMessageActivity;

    public Client(byte[] ID, byte[] publicKey, byte[] privateKey) {
        UserHolder user = new UserHolder(ID, publicKey, privateKey);
        Base64Android b = new Base64Android();

        dataHolder = new DataHolder(publicKey, privateKey);

        dataHolder.setPort(6000);
        dataHolder.setIP("192.168.43.53");
        dataHolder.setBase64(b);
        dataHolder.setServer(false);

        CryptManager manager = new CryptManager();
        manager.setKeys(publicKey, privateKey);
        dataHolder.setManager(manager);

        client = new MessageHandler(dataHolder, this, user);

        sendAuthMessage();
    }

    @Override
    public void messageReceived(final String message, Socket socket, DataHolder dataHolder) {
        System.out.println("Raw from server: " + message);

        try{
            byte[] fromID = message.split("&space&")[0].getBytes();
            String msg = message.split("&space&")[1];

            System.out.println(new String(fromID) + " says " + msg);

            //If the Message Activity for this user is open, send it on pls
            byte[] convoID = new byte[0];

            try{
                convoID = currentMessageActivity.getConversation().getProfile().getUser_ID_key();
            }catch (Exception e){
                Log.wtf("Error in messageRecieved", e.toString());
            }

            if (Arrays.equals(fromID, convoID)){
                currentMessageActivity.messageReceived(msg);
            }else{
                History his = new History(mainActivity);

                ConvoDbControl convoDbControl = new ConvoDbControl(mainActivity);

                ProfileDbControl profileDbControl = new ProfileDbControl(mainActivity);
                Profile prof = profileDbControl.selectSingleProfile(fromID);
                Conversation conversation = convoDbControl.selectProfileConvo(prof.getProfile_ID());

                Message msg2 = new Message(prof.getIcon().getIcon_ID(), false, msg, "");
                his.setConversation_ID(conversation.getConversation_ID());
                his.setMessageObj(msg2);
                his.setUser_from(prof);

                his.insertHistory();
            }

            refreshMain();

        }catch(Exception e) {
            Log.wtf("Error in messageReceived", e.toString());
        }
    }

    public void sendMessage(String message, byte[] to) {

        DataContainer data = new DataContainer();

        data.setMessage(message);
        data.setToID(to);
        //TODO set auth as false default
        data.setAuth(false);
        data.setHandler(client);

        new NetControlAsyncTask(data).execute(true, false);
    }

    private void sendAuthMessage() {
        DataContainer data = new DataContainer();

        data.setAuth(true);
        data.setHandler(client);

        new NetControlAsyncTask(data).execute(false, true);
    }

    public void stop() {
        client.stop();
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MessageActivity getCurrentMessageActivity() {
        return currentMessageActivity;
    }

    public void setCurrentMessageActivity(MessageActivity currentMessageActivity) {
        this.currentMessageActivity = currentMessageActivity;
    }

    public void refreshMain(){
        mainActivity.updateList();
    }
}

