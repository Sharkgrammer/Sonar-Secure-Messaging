package com.shark.sonar.utility;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class readFile {

    Context context;

    public readFile(Context context){
        this.context = context;
    }

    public InputStream returnAssetAsInputStream(String name) throws IOException {
        InputStream in = context.getAssets().open(name);

        if (in != null){
            return  in;
        }else{
            throw new NullPointerException();
        }
    }

    public String returnAssetAsString(String name) throws IOException {

        StringBuilder returnStr = new StringBuilder();
        String checkStr = "";

        BufferedReader bf = new BufferedReader(
                new InputStreamReader(returnAssetAsInputStream(name)
                ));

        checkStr = bf.readLine();
        do{
            returnStr.append(checkStr);
            checkStr = bf.readLine();
        }while (checkStr != null);

        if (returnStr.length() != 0){
            return returnStr.toString();
        }else{
            throw new NullPointerException();
        }
    }


}
