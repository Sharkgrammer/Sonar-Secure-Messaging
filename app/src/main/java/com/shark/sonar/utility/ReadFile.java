package com.shark.sonar.utility;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFile {

    Context context;

    public ReadFile(Context context){
        this.context = context;
    }

    //REF https://stackoverflow.com/questions/30152784/working-with-sql-file-in-android

    InputStream returnAssetAsInputStream(String name) throws IOException {
        InputStream in = context.getAssets().open(name);

        return  in;
    }

    //REF https://stackoverflow.com/questions/14825374/reading-a-textfile-using-inputstream

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
