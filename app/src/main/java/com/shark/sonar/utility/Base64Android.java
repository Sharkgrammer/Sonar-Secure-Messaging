package com.shark.sonar.utility;

import android.os.Build;
import android.util.Base64;
import util.Base64Handler;

public class Base64Android implements Base64Handler {

    public byte[] toBase64(String str) {
        return Base64.encode(str.getBytes(), Base64.NO_WRAP);
    }

    public byte[] toBase64(byte[] str) {
        return Base64.encode(str, Base64.NO_WRAP);
    }

    public byte[] fromBase64(String str) {
        return Base64.decode(str.getBytes(), Base64.NO_WRAP);
    }

    public byte[] fromBase64(byte[] str) {
        return Base64.decode(str, Base64.NO_WRAP);
    }

}
