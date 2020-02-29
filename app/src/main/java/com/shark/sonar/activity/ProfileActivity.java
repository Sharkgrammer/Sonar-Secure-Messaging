package com.shark.sonar.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.shark.sonar.R;
import com.shark.sonar.controller.ColourDbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Colour;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.IconPicker;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, view;
    private ProfileDbControl con;
    private Profile user;
    private SharedPreferences pref;
    private TextView pinView;
    private StringBuilder currentPin;
    private final int maxPinSize = 10, minPinSize = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.txtName);
        LinearLayout lay = findViewById(R.id.profileInnerIcons);
        ImageView img = findViewById(R.id.profileFinalImageView);
        view = findViewById(R.id.profileNewImageID);
        ConstraintLayout include = findViewById(R.id.userSettingsProfile);

        con = new ProfileDbControl(this);

        String title;
        int ID;
        try {
            ID = getIntent().getExtras().getInt("UserID");

            user = con.selectSingleProfile(ID);

            title = "Update profile for " + user.getName();
            include.setVisibility(View.GONE);
        } catch (Exception e) {
            user = con.selectUserProfile();
            title = "Update your profile";
            Switch pass, finger;
            Button changePass;


            pref = this.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);

            pass = findViewById(R.id.spinPIN);
            finger = findViewById(R.id.spinFinger);
            changePass = findViewById(R.id.buttonChangePin);

            pass.setChecked(!pref.getString("pin", "").equals(""));
            finger.setChecked(pref.getBoolean("fingerprint", false));

            if (!pass.isChecked()){
                finger.setVisibility(View.INVISIBLE);
                changePass.setVisibility(View.INVISIBLE);
            }

            changePass.setOnClickListener(view -> showPinDialog("Set a new PIN"));

            pass.setOnCheckedChangeListener((compoundButton, b) -> {
                if (!b){
                    pref.edit().putString("pin", "").apply();
                    finger.setChecked(false);
                    finger.setVisibility(View.INVISIBLE);
                    changePass.setVisibility(View.INVISIBLE);
                }else{
                    showPinDialog("Set a new PIN");
                    finger.setVisibility(View.VISIBLE);
                    changePass.setVisibility(View.VISIBLE);
                    pref.edit().putBoolean("unlocked", false).apply();
                }
            });

            finger.setOnCheckedChangeListener((compoundButton, b) -> pref.edit().putBoolean("fingerprint", b).apply());


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

    public void UpdateUser(View v) {

        if (name.getText().toString().equals("")) {
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

    private void showPinDialog(String title) {
        currentPin = new StringBuilder();

        LayoutInflater li = LayoutInflater.from(this);
        View dialog = li.inflate(R.layout.pin_alert, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialog);

        TextView txt = dialog.findViewById(R.id.pinAlertTitle);
        Button btn = dialog.findViewById(R.id.pinAlertButton);
        txt.setText(title);
        pinView = dialog.findViewById(R.id.txtSplashPin);

        final AlertDialog alert = alertDialogBuilder.show();
        final Context c = this;

        btn.setOnClickListener(view -> {

            if (currentPin.length() < minPinSize){
                Toast.makeText(c, "Pin must be longer then " + minPinSize + " figures", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(c, "New PIN set", Toast.LENGTH_LONG).show();
            pref.edit().putString("pin", pinView.getText().toString()).apply();
            alert.dismiss();
        });

    }

    public void pinPad(View v) {
        int len = currentPin.length();

        switch (v.getId()) {

            case R.id.splashBtn0:
                currentPin.append("0");
                break;

            case R.id.splashBtn1:
                currentPin.append("1");
                break;

            case R.id.splashBtn2:
                currentPin.append("2");
                break;

            case R.id.splashBtn3:
                currentPin.append("3");
                break;

            case R.id.splashBtn4:
                currentPin.append("4");
                break;

            case R.id.splashBtn5:
                currentPin.append("5");
                break;

            case R.id.splashBtn6:
                currentPin.append("6");
                break;

            case R.id.splashBtn7:
                currentPin.append("7");
                break;

            case R.id.splashBtn8:
                currentPin.append("8");
                break;

            case R.id.splashBtn9:
                currentPin.append("9");
                break;

            case R.id.splashBtnC:
                if (len != 0) {
                    currentPin.delete(0, len);
                }
                break;

            case R.id.splashBtnB:
                if (len != 0) {
                    currentPin.delete(len - 1, len);
                }
                break;
        }

        len = currentPin.length();

        if (len > maxPinSize) {
            currentPin.delete(len - 1, len);

            Toast.makeText(this, "Pin cannot be longer then " + maxPinSize + " figures", Toast.LENGTH_LONG).show();
        }

        pinView.setText(currentPin.toString());
    }

}
