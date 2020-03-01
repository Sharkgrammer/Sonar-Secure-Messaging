package com.shark.sonar.utility;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.shark.sonar.activity.MainActivity;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class Fingerprinter {

    private static final String KEY_NAME = "SonarFingerKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private Context c;
    private FingerprintHelper helper;
    private boolean setup;

    public Fingerprinter(Context c, boolean setup) {
        this.c = c;
        this.setup = setup;
    }

    public void silence() {
        helper.silence();
    }

    //REF https://www.androidauthority.com/how-to-add-fingerprint-authentication-to-your-android-app-747304/
    //This ref applies for pretty much the rest of the file
    public void setupFingerprinting() {
        KeyguardManager keyguardManager = (KeyguardManager) c.getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) c.getSystemService(FINGERPRINT_SERVICE);

        if ((!fingerprintManager.isHardwareDetected()) ||
                (ActivityCompat.checkSelfPermission(c, Manifest.permission.USE_FINGERPRINT)
                        != PackageManager.PERMISSION_GRANTED) ||
                (!fingerprintManager.hasEnrolledFingerprints()) || (!keyguardManager.isKeyguardSecure())) {

            if (setup)
                Toast.makeText(c, "Your device may not support fingerprint unlocking", Toast.LENGTH_LONG).show();
        } else {
            try {
                generateKey();
            } catch (Exception e) {
                Log.wtf("GenerateKey", e.toString());
            }

            if (initCipher()) {
                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                helper = new FingerprintHelper(c, setup);
                helper.startAuth(fingerprintManager, cryptoObject);

                if (setup)
                    Toast.makeText(c, "Touch your fingerprint sensor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (Exception e) {
            Log.wtf("GenerateKey", e.toString());
        }
    }

    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (Exception e) {
            Log.wtf("initCipher", e.toString());

            return false;
        }
    }

    //REF https://www.androidauthority.com/how-to-add-fingerprint-authentication-to-your-android-app-747304/
    //This entire class is based on ^
    private static class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

        private CancellationSignal cancellationSignal;
        private Context context;
        private boolean setup, silence;

        public FingerprintHelper(Context c, boolean setup) {
            this.setup = setup;
            context = c;
            silence = false;
        }

        public void silence() {
            silence = true;
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
            if (!silence)
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationFailed() {
            if (!silence)
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            SharedPreferences pref = context.getSharedPreferences("com.shark.sonar", Context.MODE_PRIVATE);

            if (!silence) {
                if (setup) {
                    Toast.makeText(context, "Fingerprint security complete!", Toast.LENGTH_LONG).show();
                    pref.edit().putBoolean("fingerprint", true).apply();
                } else {
                    Toast.makeText(context, "Fingerprint accepted", Toast.LENGTH_LONG).show();
                    pref.edit().putBoolean("unlocked", true).apply();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }

        }

    }
}
