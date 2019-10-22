package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.readFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileDbControl extends DbControl {

    public ProfileDbControl(Context c){
        super(c);
    }

    public List<Profile> selectAllprofile(){
        return selectProfile(null);
    }

    public Profile selectSignleProfile(int Profile_ID){
        return selectProfile(Profile_ID).get(0);
    }

    public Profile selectUserProfile(){
        return selectProfile(0).get(0);
    }

    private List<Profile> selectProfile(Integer profile_ID){
        List<Profile> result = new ArrayList<>();
        String name = "selectProfile", sqlFile;
        final int SELECT_ALL = 0, SELECT_USER = 1;

        try {
            readFile readFile = new readFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name +".sql").split(";");
            Cursor cursor;

            if (profile_ID != null){

                sqlFile = sqlFileAll[SELECT_USER];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(profile_ID)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }

            cursor.moveToFirst();

            Profile p;
            do{
                p = new Profile();

                p.setProfile_ID(cursor.getInt(0));
                p.setIcon(cursor.getInt(1));
                p.setName(cursor.getString(2));
                p.setUser_key_public(cursor.getBlob(3));
                p.setUser_key_private(cursor.getBlob(4));
                p.setUser_ID_key(cursor.getBlob(5));

                result.add(p);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteProfile(int profileID){
        String name = "deleteProfile";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            db.execSQL(sqlFile);
        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    public boolean insertProfile(Profile profile){
        String name = "insertProfile";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, profile, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateProfile(int profileID, Profile profile){
        String name = "updateProfile";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(profileID, profile, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer profileID, Profile profile, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, profile.getProfile_ID());
        queryState.bindDouble(2, profile.getIcon().getIcon_ID());
        queryState.bindString(3, profile.getName());
        queryState.bindBlob(4, profile.getUser_key_public());
        queryState.bindBlob(5, profile.getUser_key_private());
        queryState.bindBlob(6, profile.getUser_ID_key());

        if (profileID != null){
            queryState.bindDouble(7, profileID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
