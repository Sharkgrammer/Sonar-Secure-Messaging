package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.ReadFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileDbControl extends DbControl {

    public ProfileDbControl(Context c) {
        super(c);
    }

    public List<Profile> selectAllProfiles() {
        return selectProfile(null, null);
    }

    public Profile selectSingleProfile(int Profile_ID) {

        System.out.println("SELECT SINGLE PROFILE " + Profile_ID);

        return selectProfile(Profile_ID, null).get(0);
    }

    public Profile selectSingleProfile(byte[] User_Key) {

        System.out.println("SELECT SINGLE PROFILE " + new String(User_Key));

        return selectProfile(null, User_Key).get(0);
    }

    public Profile selectUserProfile() {
        List<Profile> profs = selectProfile(1, null);

        if (profs.isEmpty()) {
            return null;
        } else {
            return selectProfile(1, null).get(0);
        }
    }

    private List<Profile> selectProfile(Integer profile_ID, byte[] user_key) {
        List<Profile> result = new ArrayList<>();
        String name = "selectProfile", sqlFile;
        final int SELECT_ALL = 0, SELECT_USER = 1, SELECT_USER_KEY = 2;

        try {
            ReadFile readFile = new ReadFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name + ".sql").split(";");
            Cursor cursor;

            if (profile_ID != null) {

                sqlFile = sqlFileAll[SELECT_USER];
                cursor = db.rawQuery(sqlFile, new String[]{String.valueOf(profile_ID)});

            } else if (user_key != null) {

                sqlFile = sqlFileAll[SELECT_USER_KEY];
                cursor = db.rawQuery(sqlFile,  new String[]{new String(user_key)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }

            cursor.moveToFirst();

            Profile p;
            do {
                p = new Profile(context);

                p.setProfile_ID(cursor.getInt(0));
                p.setIcon(cursor.getInt(1));
                p.setName(cursor.getString(2));
                p.setUser_key_public(cursor.getBlob(3));
                p.setUser_key_private(cursor.getBlob(4));
                p.setUser_ID_key(cursor.getBlob(5));

                result.add(p);

            } while (cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteProfile(int profileID) {
        String name = "deleteProfile";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            db.execSQL(sqlFile);
        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    public boolean makeUserProfile(Profile profile) {
        return insertProfile(profile, true);
    }

    public boolean insertProfile(Profile profile) {
        return insertProfile(profile, false);
    }

    public boolean insertProfile(Profile profile, boolean userProfile) {
        String name = "insertProfile";
        long ID;

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            ID = insertUpdate(null, profile, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        System.out.println("Insert User " + ID);


        if (!userProfile) {
            System.out.println("oi");
            Conversation convo = new Conversation(context);

            Profile temp = new Profile();
            temp.setProfile_ID((int) ID);
            convo.setProfile(temp);

            convo.setBridge(null);
            convo.setColour(null);
            convo.setHistoryArrayList();

            System.out.println("USER PROFILE CONVO " + convo.getProfile().getName());

            System.out.println("CONVERSATION CREATED" + new ConvoDbControl(context).insertConvo(convo));
        }

        return true;
    }

    public boolean updateUserProfile(Profile profile) {
        return  updateProfile(1, profile);
    }

    public boolean updateProfile(int profileID, Profile profile) {
        String name = "updateProfile";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(profileID, profile, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private long insertUpdate(Integer profileID, Profile profile, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);
        int num = 1;

        queryState.bindDouble(num++, profile.getIcon().getIcon_ID());
        queryState.bindString(num++, profile.getName());
        queryState.bindBlob(num++, profile.getUser_key_public());
        queryState.bindBlob(num++, profile.getUser_key_private());
        queryState.bindBlob(num++, profile.getUser_ID_key());

        if (profileID != null) {
            queryState.bindDouble(num, profileID);
            return queryState.executeUpdateDelete();
        } else {
            long temp = queryState.executeInsert();

            return temp;
        }

    }


}
