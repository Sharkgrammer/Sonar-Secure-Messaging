package com.shark.sonar.activity;

import android.DataContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.NetControlAsyncTask;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MessageAdapter;
import com.shark.sonar.recycler.MessageViewHolder;
import com.shark.sonar.utility.Base64Android;
import com.shark.sonar.utility.Client;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import crypto.CryptManager;
import send.MessageHandler;
import util.Base64Util;
import util.DataHolder;
import util.ResultHandler;
import util.UserHolder;

public class MessageActivity extends AppCompatActivity {

    private EditText sendView;
    private Client client = MainActivity.client;
    private boolean clientOn = false, msgReceived = false;
    private Conversation conversation;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private Profile ProfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sendView = findViewById(R.id.sendView);

        ProfileDbControl ProfCon = new ProfileDbControl(this);
        ProfUser = ProfCon.selectUserProfile();

        ConvoDbControl conDB = new ConvoDbControl(this);
        conversation = conDB.selectConvoByID(Integer.parseInt((String) getIntent().getExtras().get("ID")));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(conversation.getProfile().getName());

        //REF https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<History> his = conversation.getHistoryArrayList();

        /*History his2 = new History();
        Message msg = new Message(conversation.getProfile().getIcon().getIcon_ID(), true, "Hey you!", "");
        his2.setMessageObj(msg);
        his.add(his2);*/

        conversation.setHistoryArrayList(his);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(conversation, this);
        recyclerView.setHasFixedSize(true);

        //REF https://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        LinearLayoutManager lay = new LinearLayoutManager(this);
        //lay.setReverseLayout(true);
        //lay.setStackFromEnd(true);

        recyclerView.setLayoutManager(lay);
        recyclerView.setAdapter(adapter);

        client.setCurrentMessageActivity(this);
    }

    public void stop(){
        client.stop();
    }

    public void sendMessage(View v){
        String message = sendView.getText().toString();
        client.sendMessage(message, conversation.getProfile().getUser_ID_key());

        MessageViewHolder msg = adapter.getRecentViewholder();
        if (msgReceived || adapter.getItemCount() == 0){
            History his = new History();
            Message msg2 = new Message(ProfUser.getIcon().getIcon_ID(), true, message, "");
            his.setMessageObj(msg2);

            adapter.add(his);
        }else{
            msg.addNewMessage(sendView.getText().toString());
        }
        msgReceived = false;

        sendView.setText("");
    }

    public void messageReceived(final String message) {
        System.out.println("Data from client class: " + message);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageViewHolder msgAd = adapter.getRecentViewholder();
                if (!msgReceived || adapter.getItemCount() == 0){
                    History his = new History();
                    Message msg2 = new Message(conversation.getProfile().getIcon().getIcon_ID(), false, message, "");
                    his.setMessageObj(msg2);

                    adapter.add(his);
                }else{
                    msgAd.addNewMessage(message);
                }
                msgReceived = true;
            }
        });

    }

    public Conversation getConversation(){
        return conversation;
    }
}
