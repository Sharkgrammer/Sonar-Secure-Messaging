package com.shark.sonar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Profile;

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    TextView name, key;
    ProfileDbControl con;
    Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.txtName);
        key = findViewById(R.id.txtIDKey);

        con = new ProfileDbControl(this);
        user = con.selectUserProfile();

        name.setText(user.getName()); }

    public void UpdateUser(View v){
        user.setName(name.getText().toString());
        user.setUser_ID_key(key.getText().toString().getBytes());

        boolean result = con.updateUserProfile(user);

        Toast.makeText(this, result ? "Profile Updated!" : "Profile Update failed", Toast.LENGTH_LONG).show();

        onBackPressed();
    }
}
