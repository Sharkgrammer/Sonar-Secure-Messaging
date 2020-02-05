package com.shark.sonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Profile;

import crypto.CryptManager;
import util.temp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void CreateUser(View v){
        TextView name = findViewById(R.id.txtName);

        ProfileDbControl con = new ProfileDbControl(this);
        Profile user = new Profile(this);

        CryptManager man = new CryptManager();

        user.setProfile_ID(1);
        user.setName(name.getText().toString());
        user.setUser_ID_key(man.getUserKey().getBytes());
        user.setIcon(R.drawable.ic_person6);

        //TODO redo when users can connect
        temp temp = new temp();
        user.setUser_key_private(temp.prkey1);
        user.setUser_key_public(temp.pukey1);

        boolean result = con.makeUserProfile(user);

        if (result){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        Toast.makeText(this, result ? "Profile Created!" : "Profile Create failed", Toast.LENGTH_LONG).show();

    }
}
