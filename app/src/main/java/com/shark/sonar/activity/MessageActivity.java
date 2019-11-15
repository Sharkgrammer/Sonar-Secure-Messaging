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
                new Message(R.drawable.ic_star_orange, false, "1Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "2How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "3I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "4That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "5Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "6How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "7I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "8That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "9Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "10How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "11I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "12That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "13Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "14How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "15I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "16That's great! I have a shark now and many many pizza boops so its all good", "eh"),
                new Message(R.drawable.ic_star_orange, false, "17Howdy", "eh"),
                new Message(R.drawable.ic_person_purple, true, "18How are you?", "eh"),
                new Message(R.drawable.ic_star_orange, false, "19I'm doing good! Life has done some real stuff to me lately but whateves bro", "eh"),
                new Message(R.drawable.ic_person_purple, true, "20That's great! I have a shark now and many many pizza boops so its all good", "eh"),
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MessageAdapter adapter = new MessageAdapter(data, this);
        recyclerView.setHasFixedSize(true);

        //REF https://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        LinearLayoutManager lay = new LinearLayoutManager(this);
        //lay.setReverseLayout(true);
        lay.setStackFromEnd(true);

        recyclerView.setLayoutManager(lay);
        recyclerView.setAdapter(adapter);
    }
}
