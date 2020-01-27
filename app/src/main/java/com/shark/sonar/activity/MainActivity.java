package com.shark.sonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbControl con = new DbControl(this);

        //con.deleteTables();

        if (!con.databaseExists()) {
            con.createTables();

            con.initialise();
        }

        ProfileDbControl ProfCon = new ProfileDbControl(this);

        List<Profile> profs = ProfCon.selectAllProfiles();

        for (Profile p : profs){
            System.out.println(p.getName() + " " + p.getProfile_ID() + " " + new String(p.getUser_ID_key()));;
        }

        Profile ProfUser = ProfCon.selectUserProfile();

        if (ProfUser == null) {
            startActivity(new Intent(this, SplashActivity.class));
            this.finish();
        } else {

            temp temp = new temp();

            client = new Client(ProfUser.getUser_ID_key(), temp.pukey1, temp.prkey1);

            ImageView mainView = findViewById(R.id.imgPersonMain);
            mainView.setImageDrawable(ProfUser.getIcon().getIcon());

            List<Conversation> conversations = new ConvoDbControl(this).selectAllConvo();

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


}
