package com.shark.sonar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.utility.Fingerprinter;

public class LockActivity extends AppCompatActivity {

    private TextView pinView, attemptView;
    private StringBuilder currentPin;
    private Fingerprinter fp;
    protected String pin;
    private int attempts = 3;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        pref = this.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);

        pin = pref.getString("pin", "");

        if (pin.equals("")){
            pref.edit().putBoolean("unlocked", true).apply();
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }

        boolean useFinger = pref.getBoolean("fingerprint", false);
        boolean isLocked = pref.getBoolean("locked", false);

        if (isLocked) {
            attempts = 0;
            useFinger = false;
        }

        attemptView = findViewById(R.id.lockAttemptView);

        currentPin = new StringBuilder();
        pinView = findViewById(R.id.txtSplashPin);

        if (useFinger){
            fp = new Fingerprinter(this, false);
            fp.setupFingerprinting();
        }else{
            Button btn = findViewById(R.id.btnFingerSensor);
            btn.setVisibility(View.GONE);
        }

        setViewText();

    }

    public void redoFinger(View v) {
        fp.setupFingerprinting();
    }

    public void Unlock(View v) {

        if (attempts <= 0) {
            pref.edit().putBoolean("locked", true).apply();
            Toast.makeText(this, "Device locked", Toast.LENGTH_LONG).show();
            return;
        }

        if (currentPin.toString().equals(pin)) {

            Toast.makeText(this, "Correct Pin", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean("unlocked", true).apply();
            startActivity(new Intent(this, MainActivity.class));
            this.finish();

        } else {
            Toast.makeText(this, "Incorrect pin", Toast.LENGTH_LONG).show();
            attempts--;

            setViewText();
        }
    }

    public void setViewText() {
        String temp = getResources().getString(R.string.lockSec) + " " + attempts;
        attemptView.setText(temp);
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

        pinView.setText(currentPin.toString());
    }
}
