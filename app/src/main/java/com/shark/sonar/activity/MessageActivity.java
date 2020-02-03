package com.shark.sonar.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.ColourDbControl;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Colour;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MessageAdapter;
import com.shark.sonar.recycler.MessageViewHolder;
import com.shark.sonar.utility.Client;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private EditText sendView;
    private Client client = MainActivity.client;
    private boolean clientOn = false, msgReceived = false, firstRun = true;
    private Conversation conversation;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private Profile ProfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //new ColourDbControl(this).makeSampleColours();
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

        for (History h : his) {
            System.out.println(h.getUser_from().getName());
        }

        conversation.setHistoryArrayList(his);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessageAdapter(conversation, this);
        recyclerView.setHasFixedSize(true);

        //REF https://stackoverflow.com/questions/26580723/how-to-scroll-to-the-bottom-of-a-recyclerview-scrolltoposition-doesnt-work
        LinearLayoutManager lay = new LinearLayoutManager(this);
        //lay.setReverseLayout(true);
        lay.setStackFromEnd(true);

        recyclerView.setLayoutManager(lay);
        recyclerView.setAdapter(adapter);

        client.setCurrentMessageActivity(this);
    }

    public void stop() {
        client.stop();
    }

    public void sendMessage(View v) {
        String message = sendView.getText().toString();
        client.sendMessage(message, conversation.getProfile().getUser_ID_key());

        History his = new History(this);
        Message msg2 = new Message(ProfUser.getIcon().getIcon_ID(), true, message, "");
        ;
        his.setConversation_ID(conversation.getConversation_ID());
        his.setMessageObj(msg2);
        his.setUser_from(ProfUser);

        boolean temp = his.insertHistory();
        System.out.println(temp);

        adapter.add(his);

        client.refreshMain();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        sendView.setText("");
    }

    public void messageReceived(final String message) {
        System.out.println("Data from client class: " + message);
        final Context c = this;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MessageViewHolder msgAd = adapter.getRecentViewholder();
                History his = new History(c);
                Message msg = new Message(conversation.getProfile().getIcon().getIcon_ID(), false, message, "");
                his.setConversation_ID(conversation.getConversation_ID());
                his.setMessageObj(msg);
                his.setUser_from(conversation.getProfile());

                boolean temp = his.insertHistory();

                System.out.println(temp);

                adapter.add(his);

                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                msgReceived = true;
            }
        });

    }

    public Conversation getConversation() {
        return conversation;
    }

    //REF https://stackoverflow.com/questions/37944528/add-options-in-an-option-menu-of-main-toolbar-in-android-studio
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = false;

        switch (id) {

            case R.id.theme:
                showThemeDialog();
                result = true;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

        return result;
    }

    private void showThemeDialog() {

        LayoutInflater li = LayoutInflater.from(this);
        View dialog = li.inflate(R.layout.colour_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialog);

        LinearLayout content = dialog.findViewById(R.id.colDialogContent);
        Button save = dialog.findViewById(R.id.dialogBtnSave);

        ColourDbControl dbControl = new ColourDbControl(this);

        List<Colour> colourList = dbControl.selectAllColours();

        ConstraintLayout layThem, layYou, layBack, layOverall;
        TextView txtThem, txtYou, txtBack, txtID, txtTitle;

        for (Colour c : colourList){
            View child = li.inflate(R.layout.item_single_colour, null);

            txtTitle = child.findViewById(R.id.colHeader);
            txtID = child.findViewById(R.id.colD);
            txtBack = child.findViewById(R.id.colTxtBack);
            txtYou = child.findViewById(R.id.colTxtFrom);
            txtThem = child.findViewById(R.id.colTxtTo);
            layYou = child.findViewById(R.id.colLayFrom);
            layThem = child.findViewById(R.id.colLayTo);
            layBack = child.findViewById(R.id.colLayBack);
            layOverall = child.findViewById(R.id.colLayOverall);

            txtTitle.setText(c.getCol_Name());
            txtID.setText(String.valueOf(c.getColour_ID()));
            txtYou.setTextColor(Color.parseColor(c.getText_Col()));
            txtThem.setTextColor(Color.parseColor(c.getText_Col()));
            txtBack.setTextColor(Color.parseColor(c.getText_Background_Col()));

            layBack.setBackgroundColor(Color.parseColor(c.getChat_Col_Background()));
            layYou.setBackgroundColor(Color.parseColor(c.getChat_Col_From()));
            layThem.setBackgroundColor(Color.parseColor(c.getChat_Col_To()));

            final String name = c.getCol_Name();
            final Context con = this;

            layOverall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //save
                    Toast.makeText(con, "Colour " + name + " clicked", Toast.LENGTH_SHORT).show();
                }
            });

            content.addView(child);
        }

        //alertDialogBuilder.setTitle(getResources().getString(R.string.colourDialog));

        final AlertDialog alert = alertDialogBuilder.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save
                alert.dismiss();
            }
        });
    }
}
