package com.shark.sonar.activity;

import android.content.Intent;
import android.os.Bundle;
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

import util.temp;

public class MainActivity extends AppCompatActivity {

    public static Client client;
    private MainAdapter adapter;
    private Profile ProfUser;
    private ProfileDbControl ProfCon;
    private ImageView mainView;
    private boolean auth = false;


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

        ProfCon = new ProfileDbControl(this);
        List<Profile> profs = ProfCon.selectAllProfiles();

        for (Profile p : profs){
            System.out.println(p.getName() + " " + p.getProfile_ID() + " " + new String(p.getUser_ID_key()));
        }

        ProfUser = ProfCon.selectUserProfile();

        if (ProfUser == null) {
            startActivity(new Intent(this, SplashActivity.class));
            this.finish();
        } else {

            temp temp = new temp();

            client = new Client(ProfUser.getUser_ID_key(), temp.pukey1, temp.prkey1, this);

            mainView = findViewById(R.id.imgPersonMain);
            mainView.setImageDrawable(ProfUser.getIcon().getIcon());

            List<Conversation> conversations = new ConvoDbControl(this).selectAllConvo();

            Log.wtf("CONVERSATION SIZE", String.valueOf(conversations.size()));

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.toolbar) + ": " + ProfUser.getName());

            //REF https://www.javatpoint.com/android-recyclerview-list-example
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            adapter = new MainAdapter(conversations, this);

            client.setMainActivity(this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            if (conversations.size() == 0){
                TextView txtMainAddDesc = findViewById(R.id.txtMainAddDesc);
                txtMainAddDesc.setVisibility(View.VISIBLE);
            }


            //client.messageReceived("d2&space&hello you", null, client.getDataHolder());
        }
    }

    public void updateList(){
        List<Conversation> conversations = new ConvoDbControl(this).selectAllConvo();
        adapter.updateList(conversations);
    }

    public void gotoScan(View v) {
        startActivity(new Intent(this, ScanActivity.class));
    }

    public void gotoProfile(View v) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        client.isActive(true);
        Profile temp = ProfCon.selectUserProfile();

        if (!temp.equals(ProfUser)) {
            //Update screen for new details
            ProfUser = temp;
            getSupportActionBar().setTitle(getResources().getString(R.string.toolbar) + ": " + ProfUser.getName());
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

}
