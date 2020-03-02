package com.shark.sonar.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class ImageUtil {

    public Uri getCompressUri(File f, String name, Context c) {

        try {
            byte[] bytesArray = new byte[(int) f.length()];

            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            fis.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length, options);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            String path = MediaStore.Images.Media.insertImage(c.getContentResolver(), bmp, name, null);

            return  Uri.parse(path);
        } catch (Exception e) {
            return Uri.fromFile(f);
        }

    }

    public String FileToString(File f, String name) {

        try {
            Base64Android base64Android = new Base64Android();
            byte[] bytesArray = new byte[(int) f.length()];

            FileInputStream fis = new FileInputStream(f);
            fis.read(bytesArray);
            fis.close();

            //REF https://stackoverflow.com/questions/7359173/create-bitmap-from-bytearray-in-android#7359244
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length, options);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            byte[] encodedImg = base64Android.toBase64(bytes.toByteArray());

            String img = "img::", nameDel = ":name:";

            return (img + new String(encodedImg) + nameDel + name);

        } catch (Exception e) {
            return "";
        }

    }

    public String StringToUri(String s, Context c) {

        try {

            Base64Android base64Android = new Base64Android();

            String name = s.split(":name:")[1];

            String img = s.split("img::")[1].replace(":name:" + name, "");
            byte[] content = base64Android.fromBase64(img);

            //REF https://stackoverflow.com/questions/7359173/create-bitmap-from-bytearray-in-android#7359244
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(content, 0, content.length, options);

            String path = MediaStore.Images.Media.insertImage(c.getContentResolver(), bmp, name, null);

            Uri imageUri = Uri.parse(path);

            return "img::" + imageUri.toString();

        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

    }

}
