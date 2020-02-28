package com.shark.sonar.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //new ColourDbControl(this).makeSampleColours();
        sendView = findViewById(R.id.sendView);

        ProfileDbControl ProfCon = new ProfileDbControl(this);
        ProfUser = ProfCon.selectUserProfile();

        ConvoDbControl conDB = new ConvoDbControl(this);
        String ID = (String) getIntent().getExtras().get("ID");
        conversation = conDB.selectConvoByID(Integer.parseInt(ID));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(conversation.getProfile().getName());

        HandleColours();

        //REF https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

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

        MessageActivity act = this;
        client.setCurrentMessageActivity(act);
        MainActivity.client.setCurrentMessageActivity(act);
    }

    public void stop() {
        client.stop();
    }

    public void sendMessage(View v) {
        String message = sendView.getText().toString();

        if (message.equals("")){
            return;
        }

        client.sendMessage(message, conversation.getProfile().getUser_ID_key());

        History his = new History(this);
        Message msg2 = new Message(ProfUser.getIcon().getIcon_ID(), true, message, "");
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

        this.runOnUiThread(() -> {
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
        });

    }

    public void HandleColours(){
        Colour colour = conversation.getColour();
        ConstraintLayout mainBackground = findViewById(R.id.msgOverall);
        mainBackground.setBackgroundColor(Color.parseColor(colour.getChat_Col_Background()));
        toolbar.setBackgroundColor(Color.parseColor(colour.getPrimary_Col()));
        toolbar.setTitleTextColor(Color.parseColor(colour.getText_Col()));

        Drawable back = getResources().getDrawable(R.drawable.ic_arrow_back);
        //REF https://stackoverflow.com/a/31953613/11480852
        back.setColorFilter(Color.parseColor(colour.getText_Col()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(back);

        Drawable menu = getResources().getDrawable(R.drawable.ic_menu_black);
        menu.setColorFilter(Color.parseColor(colour.getText_Col()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setOverflowIcon(menu);

        sendView.setTextColor(Color.parseColor(colour.getText_Background_Col()));
        sendView.setHintTextColor(Color.parseColor(colour.getHint_Col()));
        Drawable d = this.getDrawable(R.drawable.text_border);
        d.setColorFilter(Color.parseColor(colour.getChat_Col_Background()), PorterDuff.Mode.MULTIPLY);
        sendView.setBackground(d);

        //REF https://stackoverflow.com/a/22192691/11480852
        this.getWindow().setStatusBarColor(Color.parseColor(colour.getPrimary_Col_Dark()));
        LinearLayout lay = findViewById(R.id.messageLayOverSendView);
        lay.setBackgroundColor(Color.parseColor(colour.getPrimary_Col()));

        //REF https://stackoverflow.com/a/32031019/11480852
        FloatingActionButton fab = findViewById(R.id.msgFab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colour.getChat_Col_From())));
        fab.setImageTintList(ColorStateList.valueOf(Color.parseColor(colour.getText_Col())));

    }

    public Conversation getConversation() {
        Log.wtf("Error in getConversation", String.valueOf((conversation == null)));
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

            final Colour col = c;
            layOverall.setOnClickListener(view -> {
                conversation.setColour(col);
                adapter.changeColours(conversation);
                HandleColours();

                conversation.updateColour();
                Toast.makeText(this, c.getCol_Name() + " chosen", Toast.LENGTH_LONG).show();
            });

            content.addView(child);
        }

        //alertDialogBuilder.setTitle(getResources().getString(R.string.colourDialog));
        final AlertDialog alert = alertDialogBuilder.show();

        save.setOnClickListener(view -> alert.dismiss());
    }

    @Override
    public void onResume() {
        super.onResume();
        client.isActive(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        client.isActive(false);
    }
}
