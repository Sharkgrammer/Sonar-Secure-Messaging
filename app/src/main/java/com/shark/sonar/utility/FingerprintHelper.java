package com.shark.sonar.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.shark.sonar.activity.MainActivity;

//REF https://www.androidauthority.com/how-to-add-fingerprint-authentication-to-your-android-app-747304/
//This entire class is based on ^
public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private Context context;
    private boolean setup;

    public FingerprintHelper(Context c, boolean setup) {
        this.setup = setup;
        context = c;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        SharedPreferences pref = context.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);

        if (setup){
            Toast.makeText(context, "Fingerprint security complete!", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean("fingerprint", true).apply();
        }else{
            Toast.makeText(context, "Fingerprint accepted", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean("unlocked", true).apply();
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

}