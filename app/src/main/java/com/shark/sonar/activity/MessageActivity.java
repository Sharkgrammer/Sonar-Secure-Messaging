package com.shark.sonar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.shark.sonar.utility.Client;
import com.shark.sonar.utility.ImageUtil;

import java.io.File;
import java.net.URI;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MessageActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 500;
    private EditText sendView;
    private Client client = MainActivity.client;
    private boolean clientOn = false, msgReceived = false, firstRun = true;
    private Conversation conversation;
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private Profile ProfUser;
    private Toolbar toolbar;
    private ImageUtil imgUtil;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        c = this;
        //new ColourDbControl(this).makeSampleColours();
        sendView = findViewById(R.id.sendView);

        imgUtil = new ImageUtil();

        //REF https://developer.android.com/training/keyboard-input/style.html
        sendView.setOnEditorActionListener((v, i, e) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                sendMessage(v);
                return true;
            }
            return false;
        });

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

    //REF https://github.com/jkwiecien/EasyImage/compare/2.0.4...master
    public void openImageView(View v) {
        EasyImage.openChooserWithDocuments(this, "Camera or Gallery?", 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //REF https://github.com/jkwiecien/EasyImage/compare/2.0.4...master
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.wtf("onImagesPicked", e.toString());
                Toast.makeText(c, "Error, please try again", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                try{
                    File f = imagesFiles.get(0);
                    Uri imageURI = Uri.fromFile(f), finalUri;

                    String[] temp = imageURI.toString().split("/");
                    String file = imgUtil.FileToString(f, temp[temp.length - 1], c);
                    finalUri = imgUtil.getCompressUri();

                    sendMessage(file,"img::" + finalUri.toString());
                }catch (Exception e){
                    Log.wtf("onImagesPicked", e.toString());
                    Toast.makeText(c, "Error, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void stop() {
        client.stop();
    }

    public void sendMessage(View v) {
        String message = sendView.getText().toString();

        if (message.equals("")) {
            return;
        }

        sendMessage(message, null);
    }

    public void sendMessage(String message, String messageHis) {

        History his = new History(this);

        if (messageHis == null){
            messageHis = message;
        }

        Message msg2 = new Message(ProfUser.getIcon().getIcon_ID(), true, messageHis, "");
        his.setConversation_ID(conversation.getConversation_ID());
        his.setMessageObj(msg2);
        his.setUser_from(ProfUser);

        int ID = his.insertHistory();
        his.setHistory_ID(ID);

        client.sendMessage(message, conversation.getProfile().getUser_ID_key());

        adapter.add(his);

        client.refreshMain();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        sendView.setText("");
    }

    public void messageReceived(String message) {
        System.out.println("Data from client class: " + message);
        final Context c = this;

        if (message.length() > 5){
            if (message.substring(0, 5).equals("img::")){
                message = imgUtil.StringToUri(message, c);
            }
        }

        final String finalMsg = message;
        this.runOnUiThread(() -> {
            History his = new History(c);
            Message msg = new Message(conversation.getProfile().getIcon().getIcon_ID(), false, finalMsg, "");
            his.setConversation_ID(conversation.getConversation_ID());
            his.setMessageObj(msg);
            his.setUser_from(conversation.getProfile());

            int ID = his.insertHistory();
            his.setHistory_ID(ID);

            adapter.add(his);

            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            msgReceived = true;
        });

    }

    public void HandleColours() {
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
        assert d != null;
        d.setColorFilter(Color.parseColor(colour.getChat_Col_Background()), PorterDuff.Mode.MULTIPLY);
        sendView.setBackground(d);

        //REF https://stackoverflow.com/a/22192691/11480852
        this.getWindow().setStatusBarColor(Color.parseColor(colour.getPrimary_Col_Dark()));
        LinearLayout lay = findViewById(R.id.messageLayOverSendView);
        lay.setBackgroundColor(Color.parseColor(colour.getPrimary_Col()));

        //REF https://stackoverflow.com/a/32031019/11480852
        ImageButton fab = findViewById(R.id.msgFab), img = findViewById(R.id.msgImg);

        fab.getDrawable().setColorFilter(Color.parseColor(colour.getText_Col()), PorterDuff.Mode.MULTIPLY);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colour.getChat_Col_From())));
        img.getDrawable().setColorFilter(Color.parseColor(colour.getText_Col()), PorterDuff.Mode.MULTIPLY);
        img.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colour.getChat_Col_From())));
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

        switch (id) {

            case R.id.theme:
                showThemeDialog();
                break;

            case R.id.profile:
                Intent i = new Intent(this, ProfileActivity.class);
                i.putExtra("UserID", conversation.getProfile().getProfile_ID());

                startActivity(i);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + id);
        }

        return true;
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

        for (Colour c : colourList) {
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

        System.out.println("onResume in message");

        Profile old = conversation.getProfile();
        conversation.refreshProfile();

        if (!old.toString().equals(conversation.getProfile().toString())) {
            adapter.updateUserIcon(conversation.getProfile().getIcon().getIcon_ID());

            getSupportActionBar().setTitle(conversation.getProfile().getName());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        client.isActive(false);
    }

    public void AdapterUpdate(List<History> h) {
        conversation.setHistoryArrayList(h);
        client.refreshMain();
    }
}
