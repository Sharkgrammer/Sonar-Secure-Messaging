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
import android.widget.Toast;
import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.DbControl;
import com.shark.sonar.controller.NetControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.recycler.MainAdapter;

import java.util.List;

import util.DataHolder;

public class MainActivity extends AppCompatActivity {

    private NetControl client;
    private DataHolder server;
    private String ID = "";
    private EditText txtIDto, txtMessage, txtIDfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mainView = findViewById(R.id.imgPersonMain);
        mainView.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black, null));
        DbControl con = new DbControl(this);

        con.deleteTables();

        System.out.println(con.databaseExists());
        if (!con.databaseExists()){
            con.createTables();
        }


        ProfileDbControl control = new ProfileDbControl(this);
        Icon icon = new Icon(R.drawable.ic_star_yellow, this);
        Profile prof = new Profile(null, "Sharkie", icon, "shark".getBytes(), "shark".getBytes(), "shark".getBytes());
        control.insertProfile(prof);

        //List<Profile> profs = new ProfileDbControl(this).selectAllProfiles();

        //for (Profile p : profs){
         //   Log.wtf("PROFILE", p.getName());
        //}

        List<Conversation> conversations = new ConvoDbControl(this).selectAllConvo();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar);

        //REF https://www.javatpoint.com/android-recyclerview-list-example
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MainAdapter adapter = new MainAdapter(conversations, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void setID(View v){
        //if (!ID.equals("")) client.stop();

        ID = txtIDfrom.getText().toString();
        client = new NetControl(this, server, ID);

        client.sendAuthMessage();

        Toast.makeText(this, "ID set to " + ID, Toast.LENGTH_SHORT).show();
    }


    public void sendMessage(View v){
        String message = txtMessage.getText().toString(), to = txtIDto.getText().toString();
        client.sendMessage(message, to.getBytes());
        Toast.makeText(this, "Message: " + message + " sent to " + to, Toast.LENGTH_SHORT).show();
    }

    public void gotoScan(View v){
        startActivity(new Intent(this, ScanActivity.class));
    }

}
