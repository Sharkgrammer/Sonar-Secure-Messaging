package com.shark.sonar.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.IconPicker;

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    TextView name, view;
    ProfileDbControl con;
    Profile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.txtName);
        LinearLayout lay = findViewById(R.id.profileInnerIcons);
        ImageView img = findViewById(R.id.profileFinalImageView);
        view = findViewById(R.id.profileNewImageID);

        con = new ProfileDbControl(this);

        String title;
        int ID;
        try{
            ID = getIntent().getExtras().getInt("UserID");

            user = con.selectSingleProfile(ID);

            title = "Update profile for " + user.getName();
        }catch (Exception e){
            user = con.selectUserProfile();
            title = "Update your profile";
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        Drawable back = getResources().getDrawable(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(back);

        //REF https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        name.setText(user.getName());
        img.setImageDrawable(user.getIcon().getIcon());
        view.setText(String.valueOf(user.getIcon().getIcon_ID()));

        new IconPicker(lay, img, view, this);
    }

    public void UpdateUser(View v){

        if (name.getText().toString().equals("")){
            Toast.makeText(this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setName(name.getText().toString());
        Icon icon = new Icon(Integer.parseInt(view.getText().toString()));
        user.setIcon(icon);

        boolean result = con.updateProfile(user.getProfile_ID(), user);

        Toast.makeText(this, result ? "Profile Updated!" : "Profile Update failed", Toast.LENGTH_LONG).show();

        onBackPressed();
    }
}
