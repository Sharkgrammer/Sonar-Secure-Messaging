package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shark.sonar.utility.readFile;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
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
        //not called
    }

    public Boolean databaseExists(){
        Boolean result = false;

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString("check_tables.sql");

            Cursor cursor = db.rawQuery(sqlFile, null);
            if (cursor.moveToFirst())
            {
                int count = cursor.getInt(0);

                result = count > 0;
            }

            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Boolean createTables(){
        Boolean result = false;

        if (!databaseExists()){
            try {
                readFile readFile = new readFile(context);

                String sqlFile = readFile.returnAssetAsString("table_creates.sql");

                db.execSQL(sqlFile);

                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void runDatabaseTest(){

        Log.wtf("TestStart", "Test has Started");

        try {
            readFile readFile = new readFile(context);

            db.execSQL(readFile.returnAssetAsString("testCreate.sql"));
            Log.wtf("TablesCreated", "Tables Created");

            for (String x : readFile.returnAssetAsString("testInsert.sql").split(";")){
                db.execSQL(x);
            }

            Log.wtf("TablesInserted", "Tables Inserted Into");

            Cursor cursor = db.rawQuery(readFile.returnAssetAsString("testSelect.sql"), null);
            Log.wtf("Select Run", "Count is " + String.valueOf(cursor.getCount()) + " and is closed? " + String.valueOf(cursor.isClosed()));

            cursor.moveToFirst();
            do{
                Log.wtf("Test Item Output", cursor.getString(0));
            }while(cursor.moveToNext());
            cursor.close();

            db.execSQL(readFile.returnAssetAsString("testDel.sql"));
            Log.wtf("TablesDeleted", "Tables Deleted");

        } catch (IOException e) {
            Log.wtf("Error", e.toString());
        }

        Log.wtf("TestEnd", "Test has Ended");

    }


}
