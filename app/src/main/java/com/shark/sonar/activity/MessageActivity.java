package com.shark.sonar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
import com.shark.sonar.recycler.MessageAdapter;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private EditText sendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ConvoDbControl conDB = new ConvoDbControl(this);
        Conversation conversation = conDB.selectConvoByID(Integer.parseInt((String) getIntent().getExtras().get("ID")));

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

        History his2 = new History();
        Message msg = new Message(conversation.getProfile().getIcon().getIcon_ID(), true, "Hey you!", "");
        his2.setMessageObj(msg);
        his.add(his2);

        conversation.setHistoryArrayList(his);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MessageAdapter adapter = new MessageAdapter(conversation, this);
        recyclerView.setHasFixedSize(true);

        //REF https://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        LinearLayoutManager lay = new LinearLayoutManager(this);
        //lay.setReverseLayout(true);
        //lay.setStackFromEnd(true);

        recyclerView.setLayoutManager(lay);
        recyclerView.setAdapter(adapter);
    }
}
