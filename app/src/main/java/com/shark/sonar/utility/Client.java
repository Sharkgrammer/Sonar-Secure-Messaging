package com.shark.sonar.utility;

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

import java.net.Socket;
import java.util.Arrays;

import crypto.CryptManager;
import send.MessageHandler;
import util.DataHolder;
import util.ResultHandler;
import util.URLHandler;
import util.UserHolder;

public class Client implements ResultHandler {

    private MessageHandler client;
    private DataHolder dataHolder;
    private MainActivity mainActivity;
    private MessageActivity currentMessageActivity;
    private boolean isActive;
    private boolean auth = false;
    private boolean setPref = false;

    public Client(byte[] ID, byte[] publicKey, byte[] privateKey, String IP, int port) {

        UserHolder user = new UserHolder(ID, publicKey, privateKey);
        Base64Android b = new Base64Android();
        URLHandler u = new URLAndroid();

        dataHolder = new DataHolder(publicKey, privateKey);

        dataHolder.setBase64(b);
        dataHolder.setServer(false);
        dataHolder.setUrl(u);

        dataHolder.setPort(port);
        dataHolder.setIP(IP);

        setPref = IP == null;

        dataHolder.setAuthServer();

        CryptManager manager = new CryptManager();
        manager.setKeys(publicKey, privateKey);
        dataHolder.setManager(manager);

        client = new MessageHandler(dataHolder, this, user);

        if (!auth) {
            sendAuthMessage();
            auth = true;
        }
    }

    @Override
    public void messageReceived(final String message, Socket socket, DataHolder dataHolder) {
        System.out.println("Raw from server: " + message);

        try {
            byte[] fromID = message.split("&space&")[0].getBytes();
            String msg = message.split("&space&")[1];

            System.out.println(new String(fromID) + " says " + msg);

            //If the Message Activity for this user is open, send it on pls
            byte[] convoUserID = new byte[0];

            try {
                convoUserID = currentMessageActivity.getConversation().getProfile().getUser_ID_key();
            } catch (Exception e) {
                Log.wtf("Error in messageReceived", e.toString());
            }

            ProfileDbControl profileDbControl = new ProfileDbControl(mainActivity);
            Profile prof = profileDbControl.selectSingleProfile(fromID);
            profileDbControl.destroy();

            //TODO test this pls
            System.out.println(new String(fromID));
            System.out.println(new String(convoUserID));

            System.out.println(Arrays.toString(fromID));
            System.out.println(Arrays.toString(convoUserID));

            if (Arrays.equals(fromID, convoUserID)) {
                currentMessageActivity.messageReceived(msg);
            } else {
                History his = new History(mainActivity);

                ConvoDbControl convoDbControl = new ConvoDbControl(mainActivity);
                Conversation conversation = convoDbControl.selectProfileConvo(prof.getProfile_ID());
                convoDbControl.destroy();

                Message msg2 = new Message(prof.getIcon().getIcon_ID(), false, msg, "");
                his.setConversation_ID(conversation.getConversation_ID());
                his.setMessageObj(msg2);
                his.setUser_from(prof);

                his.insertHistory();
            }

            if (!isActive) {
                mainActivity.notifyUser("Message from: " + prof.getName(), msg);
            }

            refreshMain();

        } catch (Exception e) {
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

        if (setPref) {
            mainActivity.setIP(dataHolder.getIP(), dataHolder.getPort());
        }
    }

    public MessageActivity getCurrentMessageActivity() {
        return currentMessageActivity;
    }

    public void setCurrentMessageActivity(MessageActivity currentMessageActivity) {
        this.currentMessageActivity = currentMessageActivity;
    }

    public void refreshMain() {
        mainActivity.updateList();
    }

    public void isActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setUserToSendTo(Profile p) {
        dataHolder.setUserTo(p.getUserHolder());
    }
}

