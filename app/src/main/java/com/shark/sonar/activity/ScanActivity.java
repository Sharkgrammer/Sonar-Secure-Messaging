package com.shark.sonar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shark.sonar.R;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.Base64Android;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ScanActivity extends AppCompatActivity {
    private ProfileDbControl control;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Button mainBut = findViewById(R.id.scannerButton);

        qrScan = new IntentIntegrator(this);
        mainBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

        control = new ProfileDbControl(this);

        Profile currentUser = control.selectUserProfile();

        ImageView view = findViewById(R.id.scannerImg);
        int smallerDimension = 1000;

        String input = compileQRData(currentUser);

        Bitmap bitmap;

        QRGEncoder qrgEncoder = new QRGEncoder(input, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(getResources().getColor(R.color.colorPrimary));
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            bitmap = qrgEncoder.getBitmap();
            view.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v("ERROR", e.toString());
        }
    }

    public String compileQRData(Profile user) {
        String result, SpaceDel = "&space&",
                ID = new String(user.getUser_ID_key()), key, icon;

        Base64Android base64 = new Base64Android();
        key = new String(base64.toBase64(user.getUser_key_public()));

        icon = String.valueOf(user.getIcon().getIcon_ID());

        result = ID + SpaceDel + icon + SpaceDel + key;

        return result;
    }

    public void addUser(View v) {
        TextView name = findViewById(R.id.txtName);
        TextView key = findViewById(R.id.txtIDKey);

        Icon icon = new Icon(R.drawable.ic_star3, this);
        Profile prof = new Profile(null, name.getText().toString(), icon, "shark".getBytes(),
                "shark".getBytes(), key.getText().toString().getBytes());

        control.insertProfile(prof);

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "QR code unread", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Log.wtf("QR RESULT", result.getContents());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
