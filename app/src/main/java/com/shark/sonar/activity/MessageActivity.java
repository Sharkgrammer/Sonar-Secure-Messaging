package com.shark.sonar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.shark.sonar.R;
import com.shark.sonar.data.Message;
import com.shark.sonar.recycler.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    private EditText sendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kate");

        //REF https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Message[] data = new Message[] {
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "That's great! I have a shark now and many many pizza boops so its all good", "eh")
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MessageAdapter adapter = new MessageAdapter(data, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
