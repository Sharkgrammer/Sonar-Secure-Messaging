package com.shark.sonar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.IconDbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.Base64Android;

import java.util.Arrays;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ScanActivity extends AppCompatActivity {
    private ProfileDbControl control;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final int CAMARA_REQ = 500;
    private Button mainBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //getSupportActionBar().setTitle(getResources().getString(R.string.toolbarScanner));

        mainBut = findViewById(R.id.scannerButton);

        final Context context = this;
        final ScanActivity act = this;
        mainBut.setOnClickListener(view -> {

            //REF https://developer.android.com/training/permissions/requesting.html
            int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

            Log.wtf("CHECK", String.valueOf(perm == PackageManager.PERMISSION_GRANTED));

            if (perm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, CAMARA_REQ);
                perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            }

            if (perm == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(ScanActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }

        });


        control = new ProfileDbControl(this);

        Profile currentUser = control.selectUserProfile();

        ImageView view = findViewById(R.id.scannerImg);
        int smallerDimension = 600;

        String input = compileQRData(currentUser);

        Bitmap bitmap;

        //REF https://github.com/androidmads/QRGenerator
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

    //REF https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMARA_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainBut.callOnClick();
                } else {
                    Toast.makeText(this, "Please enable camara to add friends", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public String compileQRData(Profile user) {
        String result, SpaceDel = "&space&", name = user.getName(),
                ID = new String(user.getUser_ID_key()), key, icon;

        Base64Android base64 = new Base64Android();
        key = new String(base64.toBase64(user.getUser_key_public()));

        icon = String.valueOf(user.getIcon().getIcon_ID());

        result = name + SpaceDel + ID + SpaceDel + icon + SpaceDel + key;

        return result;
    }

    public void addUser(String name, String IDKey, String IconID, String publicKey) {

        ConvoDbControl convoDbControl = new ConvoDbControl(this);

        Conversation c = convoDbControl.selectProfileConvo(IDKey.getBytes());

        if (c != null){
            Toast.makeText(this, "Cannot make a conversation that already exists", Toast.LENGTH_LONG).show();
            return;
        }

        Base64Android base64 = new Base64Android();

        byte[] pub = base64.fromBase64(publicKey);

        Icon icon = new Icon(Integer.parseInt(IconID), this);
        Profile prof = new Profile(null, name, icon, pub, pub, IDKey.getBytes());

        boolean res = control.insertProfile(prof);

        if (res){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    //REF https://github.com/blikoon/QRCodeScanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
           Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)  return;

            String spaceDel = "&space&";

            //I hate this, but its needed to get the QR data back
            String[] result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult").split(spaceDel);

            //Toast.makeText(this, Arrays.toString(result), Toast.LENGTH_LONG).show();


            Log.wtf("RESULT", Arrays.toString(result));

            String name, userID, key, iconID;
            name = result[0];
            userID = result[1];
            iconID = result[2];
            key = result[3];

            addUser(name, userID, iconID, key);

        }
    }

}
