package com.shark.sonar.utility;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import util.URLHandler;

public class URLAndroid implements URLHandler  {

    @Override
    public StringBuilder returnStringBuilder(String url) {

        URLAndroidInternal urlTask = new URLAndroidInternal();

        StringBuilder JSONStr;
        try{
            JSONStr = urlTask.execute(url).get();
        }catch(Exception e){
            Log.wtf("Error in returnStringBuilder", e.toString());
            return null;
        }

        return JSONStr;
    }
}

class URLAndroidInternal extends AsyncTask<String, Void, StringBuilder>{

    @Override
    protected StringBuilder doInBackground(String... strings) {
        String URL = strings[0];
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        }catch (Exception e){
            Log.wtf("Error in doInBackground", e.toString());
            return null;
        }

        return response;
    }
}
