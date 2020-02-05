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
import com.shark.sonar.controller.IconDbControl;
import com.shark.sonar.controller.ProfileDbControl;
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
    private IntentIntegrator qrScan;
    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //getSupportActionBar().setTitle(getResources().getString(R.string.toolbarScanner));

        Button mainBut = findViewById(R.id.scannerButton);

        final Context context = this;
        final ScanActivity act = this;
        mainBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

                Log.wtf("CHECK", String.valueOf(perm == PackageManager.PERMISSION_GRANTED));

                if (perm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, 50);
                    perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                }

                if (perm == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(ScanActivity.this, QrCodeActivity.class);
                    startActivityForResult(i, REQUEST_CODE_QR_SCAN);
                }

            }
        });


        control = new ProfileDbControl(this);

        Profile currentUser = control.selectUserProfile();

        ImageView view = findViewById(R.id.scannerImg);
        int smallerDimension = 600;

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
        String result, SpaceDel = "&space&", name = user.getName(),
                ID = new String(user.getUser_ID_key()), key, icon;

        Base64Android base64 = new Base64Android();
        key = new String(base64.toBase64(user.getUser_key_public()));

        icon = String.valueOf(user.getIcon().getIcon_ID());

        result = name + SpaceDel + ID + SpaceDel + icon + SpaceDel + key;

        return result;
    }

    public void addUser(String name, String IDKey, String IconID, String publicKey) {

       ///*
        Base64Android base64 = new Base64Android();

        byte[] pub = base64.fromBase64(publicKey);

        Log.wtf("CHECKING ICONS", "CHECKING");

        int iconID = Integer.parseInt(IconID);
        IconDbControl db = new IconDbControl(this);

        List<Icon> list = db.selectAllIcons();

        for (Icon i : list){

            if (i.getIcon_ID() == iconID){
                Log.wtf("CHECKING ICONS", "Found a match with " + i.getIcon_ID());
            }

        }


        Log.wtf("CHECKING ICONS", "Complete");


        Icon icon = new Icon(Integer.parseInt(IconID), this);
        Profile prof = new Profile(null, name, icon, pub, pub, IDKey.getBytes());

        boolean res = control.insertProfile(prof);

        if (res){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }//*/

        /*Icon icon = new Icon(R.drawable.ic_star3, this);
        Profile prof = new Profile(null, name, icon, "shark".getBytes(),
                "shark".getBytes(), IDKey.getBytes());*/
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

            Toast.makeText(this, Arrays.toString(result), Toast.LENGTH_LONG).show();


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
