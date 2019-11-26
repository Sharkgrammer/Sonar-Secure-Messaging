package com.shark.sonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void addUser(View v){
        ProfileDbControl control = new ProfileDbControl(this);

        TextView name = findViewById(R.id.txtName);
        TextView key = findViewById(R.id.txtIDKey);

        Icon icon = new Icon(R.drawable.ic_star3, this);
        Profile prof = new Profile(null, name.getText().toString(), icon, "shark".getBytes(),
                "shark".getBytes(), key.getText().toString().getBytes());

        control.insertProfile(prof);

        startActivity(new Intent(this, MainActivity.class));
    }
}
