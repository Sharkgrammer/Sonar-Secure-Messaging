package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shark.sonar.data.Icon;
import com.shark.sonar.utility.ReadFile;

import java.util.List;

//REF https://github.com/Sharkgrammer/Android-Assistant/blob/master/app/src/main/java/com/shark/assistant/database.java

public class DbControl extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";
    SQLiteDatabase db;
    Context context;

    public DbControl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //REF https://stackoverflow.com/questions/1601151/how-do-i-check-in-sqlite-whether-a-table-exists#1604121
    public boolean databaseExists() {
        boolean result = false;

        try {
            ReadFile readFile = new ReadFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString("checkTables.sql").split(";");

            Cursor cursor;
            for (String sqlFile : sqlFileAll) {
                cursor = db.rawQuery(sqlFile, null);
                if (cursor.moveToFirst()) {
                    int count = cursor.getInt(0);

                    result = count != 0;
                }
                cursor.close();

                if (!result) break;
            }

        } catch (Exception e) {
            Log.wtf("Error in databaseExists", e.toString());
        }

        return result;
    }

    public boolean createTables() {
        boolean result = false;

        if (!databaseExists()) {
            try {
                ReadFile readFile = new ReadFile(context);

                String[] sqlFileAll = readFile.returnAssetAsString("tableCreates.sql").split(";");

                for (String sqlFile : sqlFileAll) {
                    db.execSQL(sqlFile);
                }

                result = true;
            } catch (Exception e) {
                Log.wtf("Error in createTables", e.toString());
            }
        }

        return result;
    }

    public boolean deleteTables() {
        boolean result = false;

        try {
            ReadFile readFile = new ReadFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString("deleteTables.sql").split(";");

            for (String sqlFile : sqlFileAll) {
                db.execSQL(sqlFile);
            }

            result = true;
        } catch (Exception e) {
            Log.wtf("Error in deleteTables", e.toString());
        }


        return result;
    }

    public void initialise(){
        IconDbControl con = new IconDbControl(context);
        int noOfIcons = 6;
        String[] names = {"star", "person"};
        Icon icon;

        //REF https://stackoverflow.com/questions/9948105/android-how-to-iterate-an-r-drawable-object#11318781
        for (String name : names){
            for (int x = 1; x <= noOfIcons; x++){
                icon = new Icon(context.getResources().getIdentifier("ic_" + name + x, "drawable" , context.getPackageName()));
                con.insertIcon(icon);
            }
        }


        new ColourDbControl(context).makeSampleColours(false);
    }

    public void runDatabaseTest() {

        Log.wtf("TestStart", "Test has Started");

        try {
            ReadFile readFile = new ReadFile(context);

            db.execSQL(readFile.returnAssetAsString("testCreate.sql"));
            Log.wtf("TablesCreated", "Tables Created");

            for (String x : readFile.returnAssetAsString("testInsert.sql").split(";")) {
                db.execSQL(x);
            }

            Log.wtf("TablesInserted", "Tables Inserted Into");

            Cursor cursor = db.rawQuery(readFile.returnAssetAsString("testSelect.sql"), null);
            Log.wtf("Select Run", "Count is " + String.valueOf(cursor.getCount()) + " and is closed? " + String.valueOf(cursor.isClosed()));

            cursor.moveToFirst();
            do {
                Log.wtf("Test Item Output", cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();

            db.execSQL(readFile.returnAssetAsString("testDel.sql"));
            Log.wtf("TablesDeleted", "Tables Deleted");

        } catch (Exception e) {
            Log.wtf("Error in Test", e.toString());
        }

        Log.wtf("TestEnd", "Test has Ended");

    }


}
