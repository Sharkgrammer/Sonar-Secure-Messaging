package com.shark.sonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.DbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MainAdapter;

import java.util.List;

import util.DataHolder;

public class MainActivity extends AppCompatActivity {

    private DataHolder server;
    private EditText txtIDto, txtMessage, txtIDfrom;

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
        Profile ProfUser = ProfCon.selectUserProfile();

        if (ProfUser == null) {
            startActivity(new Intent(this, SplashActivity.class));
            this.finish();
        } else {

            ImageView mainView = findViewById(R.id.imgPersonMain);
            mainView.setImageDrawable(ProfUser.getIcon().getIcon());
              /*
        ProfileDbControl control = new ProfileDbControl(this);
        Icon icon = new Icon(R.drawable.ic_star_yellow, this);
        Profile prof = new Profile(null, "Sharkie", icon, "shark".getBytes(), "shark".getBytes(), "shark".getBytes());
        control.makeUserProfile(prof);
        //*/

            //ProfileDbControl control = new ProfileDbControl(this);
            //Profile prof = control.selectSingleProfile(1);
            //System.out.println("OLD PROFILE " + prof.getName());

            List<Conversation> conversations = new ConvoDbControl(this).selectAllConvo();

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.toolbar) + ": " + ProfUser.getName());

            //REF https://www.javatpoint.com/android-recyclerview-list-example
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            MainAdapter adapter = new MainAdapter(conversations, this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

    }


    public void gotoScan(View v) {
        startActivity(new Intent(this, ScanActivity.class));
    }

    public void gotoProfile(View v) {
        startActivity(new Intent(this, ProfileActivity.class));
    }


}
