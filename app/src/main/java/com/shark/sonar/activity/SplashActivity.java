package com.shark.sonar.activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.FingerprintHelper;
import com.shark.sonar.utility.Fingerprinter;
import com.shark.sonar.utility.IconPicker;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import crypto.CryptManager;
import util.temp;

public class SplashActivity extends AppCompatActivity {

    private TextView view, pinView;
    private StringBuilder currentPin;
    private final int maxPinSize = 10, minPinSize = 4;
    private ConstraintLayout profile, lockSetup, start;
    private Fingerprinter fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        currentPin = new StringBuilder();
        pinView = findViewById(R.id.txtSplashPin);
        profile = findViewById(R.id.splashProfileConstraint);
        lockSetup = findViewById(R.id.splashSecurityConstraintCreate);
        start = findViewById(R.id.splashLogoConstraint);

        LinearLayout lay = findViewById(R.id.splashInnerIcons);
        ImageView img = findViewById(R.id.splashFinalImageView);
        view = findViewById(R.id.splashNewImageID);
        view.setText(String.valueOf(R.drawable.ic_person1));

        new IconPicker(lay, img, view, this);
    }

    public void CreateUser(View v) {
        TextView name = findViewById(R.id.txtName);

        if (name.getText().toString().equals("")) {
            Toast.makeText(this, "Name cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfileDbControl con = new ProfileDbControl(this);
        Profile user = new Profile(this);

        CryptManager man = new CryptManager();

        user.setProfile_ID(1);
        user.setName(name.getText().toString());
        user.setUser_ID_key(man.getUserKey().getBytes());

        Icon icon = new Icon(Integer.parseInt(view.getText().toString()));
        user.setIcon(icon);

        //TODO redo when users can connect
        temp temp = new temp();
        user.setUser_key_private(temp.prkey1);
        user.setUser_key_public(temp.pukey1);

        boolean result = con.makeUserProfile(user);

        if (result) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        //Toast.makeText(this, result ? "Profile Created!" : "Profile Create failed", Toast.LENGTH_LONG).show();

    }

    public void Save(View v) {

        if (currentPin.length() < minPinSize){
            Toast.makeText(this, "Pin must be longer then 4 figures", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences pref = this.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);
        pref.edit().putString("pin", currentPin.toString()).apply();
        Skip(v);
    }

    public void Start(View v) {
        lockSetup.setVisibility(View.VISIBLE);
        start.setVisibility(View.GONE);
    }

    public void Skip(View v) {
        profile.setVisibility(View.VISIBLE);
        lockSetup.setVisibility(View.GONE);
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

    public void FingerTest(View v) {
        if (fp == null){
            fp = new Fingerprinter(this);
        }

        fp.setupFingerprinting();
    }

}
