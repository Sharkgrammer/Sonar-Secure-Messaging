package com.shark.sonar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.ProfileDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Icon;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.Base64Android;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ScanActivity extends AppCompatActivity {
    private ProfileDbControl control;
    private Profile currentUser;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final int CAMERA_REQ = 500, IO_REQ = 600;
    private Button mainBut, mainShare;
    private Bitmap QRCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        mainBut = findViewById(R.id.scannerButton);
        mainShare = findViewById(R.id.shareButton);

        final Context context = this;
        final ScanActivity act = this;
        mainBut.setOnClickListener(view -> {

            //REF https://developer.android.com/training/permissions/requesting.html
            int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

            if (perm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA}, CAMERA_REQ);
            } else {
                Intent i = new Intent(ScanActivity.this, QrCodeActivity.class);
                startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            }

        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your QR code");

        Drawable back = getResources().getDrawable(R.drawable.ic_arrow_back);
        toolbar.setNavigationIcon(back);

        //REF https://freakycoder.com/android-notes-24-how-to-add-back-button-at-toolbar-941e6577418e
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        control = new ProfileDbControl(this);

        currentUser = control.selectUserProfile();

        ImageView imageView = findViewById(R.id.scannerImg);
        int smallerDimension = 500;

        String input = compileQRData(currentUser);

        QRCodeBitmap = null;

        //REF https://github.com/androidmads/QRGenerator
        QRGEncoder qrgEncoder = new QRGEncoder(input, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(getResources().getColor(R.color.colorPrimary));
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            QRCodeBitmap = qrgEncoder.getBitmap();
            imageView.setImageBitmap(QRCodeBitmap);
        } catch (Exception e) {
            Log.v("ERROR", e.toString());
        }


        mainShare.setOnClickListener(view -> {

            //REF https://developer.android.com/training/permissions/requesting.html
            int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (perm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, IO_REQ);
            } else {

                //REF https://stackoverflow.com/a/30096845/11480852
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                QRCodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), QRCodeBitmap, "Sonar_QR_Code", null);

                Uri imageUri =  Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(share, "Share QR Code"));
            }

        });
    }

    //REF https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainBut.callOnClick();
                } else {
                    Toast.makeText(this, "Please enable camera to add friends", Toast.LENGTH_LONG).show();
                }
                break;
            }

            case IO_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainShare.callOnClick();
                } else {
                    Toast.makeText(this, "Please accept to be able to share your QR code", Toast.LENGTH_LONG).show();
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

        if (Arrays.equals(IDKey.getBytes(), currentUser.getUser_ID_key())){
            Toast.makeText(this, "Cannot make a conversation with youtself", Toast.LENGTH_LONG).show();
            return;
        }

        ConvoDbControl convoDbControl = new ConvoDbControl(this);
        Conversation c = convoDbControl.selectProfileConvo(IDKey.getBytes());

        if (c != null) {
            Toast.makeText(this, "Cannot make a conversation that already exists", Toast.LENGTH_LONG).show();
            return;
        }

        Base64Android base64 = new Base64Android();

        byte[] pub = base64.fromBase64(publicKey);

        Icon icon = new Icon(Integer.parseInt(IconID), this);
        Profile prof = new Profile(null, name, icon, pub, pub, IDKey.getBytes());

        boolean res = control.insertProfile(prof);

        if (res) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    //REF https://github.com/blikoon/QRCodeScanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null) return;

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
