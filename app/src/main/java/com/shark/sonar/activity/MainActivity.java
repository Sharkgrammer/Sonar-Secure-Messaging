package com.shark.sonar.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.DbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MainAdapter;
import com.shark.sonar.utility.Client;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Client client;
    private int ConversationSize, notificationId = 0;
    private MainAdapter adapter;
    private Profile ProfUser;
    private ProfileDbControl ProfCon;
    private ImageView mainView;
    private TextView txtMainAddDesc;
    private SharedPreferences prefs;
    private boolean setPin = true;
    private String CHANNEL_ID = "Sonar-Notification-Channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        DbControl con = new DbControl(this);

        //con.deleteTables();

        if (!con.databaseExists()) {
            con.createTables();

            con.initialise();
        }

        con.destroy();
        setUpNotificationChannel();

        prefs = this.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);
        setPin = !prefs.getString("pin", "").equals("");

        txtMainAddDesc = findViewById(R.id.txtMainAddDesc);

        ProfCon = new ProfileDbControl(this);
        List<Profile> profs = ProfCon.selectAllProfiles();

        for (Profile p : profs){
            System.out.println(p.getName() + " " + p.getProfile_ID() + " " + new String(p.getUser_ID_key()));
        }

        ProfUser = ProfCon.selectUserProfile();

        if (ProfUser == null) {

            startActivity(new Intent(this, SplashActivity.class));
            this.finish();

        } else if (!prefs.getBoolean("unlocked", true)){

            startActivity(new Intent(this, LockActivity.class));
            this.finish();

        } else {

            int port = prefs.getInt("port", 0);
            String IP = prefs.getString("IP", null);

            client = new Client(ProfUser.getUser_ID_key(), ProfUser.getUser_key_public(), ProfUser.getUser_key_private(), IP, port);

            mainView = findViewById(R.id.imgPersonMain);
            mainView.setImageDrawable(ProfUser.getIcon().getIcon());

            ConvoDbControl convoDbControl = new ConvoDbControl(this);
            List<Conversation> conversations = convoDbControl.selectAllConvo();
            convoDbControl.destroy();

            Log.wtf("CONVERSATION SIZE", String.valueOf(conversations.size()));

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(ProfUser.getName());

            //REF https://www.javatpoint.com/android-recyclerview-list-example
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            adapter = new MainAdapter(conversations, this);

            client.setMainActivity(this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            ConversationSize = conversations.size();
            updateMainDesc(false);

            //client.messageReceived("d2&space&hello you", null, client.getDataHolder());
        }
    }

    public void updateList(){
        ConvoDbControl convoDbControl = new ConvoDbControl(this);
        List<Conversation> conversations = convoDbControl.selectAllConvo();
        convoDbControl.destroy();
        adapter.updateList(conversations);
    }

    public void gotoScan(View v) {
        startActivity(new Intent(this, ScanActivity.class));
    }

    public void gotoProfile(View v) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("UserID", 0);

        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMainDesc(false);

        client.isActive(true);
        client.refreshMain();

        Profile temp = ProfCon.selectUserProfile();

        if (!temp.equals(ProfUser)) {
            //Update screen for new details
            ProfUser = temp;
            getSupportActionBar().setTitle(ProfUser.getName());
            mainView.setImageDrawable(ProfUser.getIcon().getIcon());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        client.isActive(false);
    }

    @Override
    public void onNewIntent(Intent i){
        init();
    }

    public void updateMainDesc(Boolean CalledByAdapter){
        if (CalledByAdapter){
            ConversationSize = 0;
        }

        if (ConversationSize == 0){
            txtMainAddDesc.setVisibility(View.VISIBLE);
        }else{
            txtMainAddDesc.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (setPin) prefs.edit().putBoolean("unlocked", false).apply();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ProfCon.destroy();
    }

    //REF https://developer.android.com/training/notify-user/build-notification
    private void setUpNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.getResources().getString(R.string.channelName);
            String description = this.getResources().getString(R.string.channelDesc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void notifyUser(String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sonar_alone)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    public void setIP(String IP, int Port) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("IP", IP);
        editor.putInt("port", Port);

        editor.apply();
    }


}
