package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Conversation;
import com.shark.sonar.utility.readFile;

import java.util.ArrayList;
import java.util.List;

public class ConvoDbControl extends DbControl {

    public ConvoDbControl(Context c){
        super(c);
    }

    public List<Conversation> selectAllConvo(){
        return selectConversation(null, null);
    }

    public Conversation selectProfileConvo(int Profile_ID){
        return selectConversation(Profile_ID, null).get(0);
    }

    public Conversation selectConvoByID(int Convo_ID){
        return selectConversation(null, Convo_ID).get(0);
    }

    private List<Conversation> selectConversation(Integer profile_ID, Integer convo_ID){
        List<Conversation> result = new ArrayList<>();
        String name = "selectProfile", sqlFile;
        final int SELECT_ALL = 0, SELECT_PROFILE = 1, SELECT_CONVO = 2;

        try {
            readFile readFile = new readFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name +".sql").split(";");
            Cursor cursor;

            if (profile_ID != null){

                sqlFile = sqlFileAll[SELECT_PROFILE];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(profile_ID)});

            }else if(convo_ID != null){

                sqlFile = sqlFileAll[SELECT_CONVO];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(convo_ID)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }

            cursor.moveToFirst();

            Conversation c;
            do{
                c = new Conversation();

                c.setConversation_ID(cursor.getInt(0));
                c.setColour(cursor.getInt(1));
                c.setBridge(cursor.getInt(2));
                c.setProfile(cursor.getInt(3));

                result.add(c);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteConvo(int ConvoID){
        String name = "deleteConvo";

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

    public boolean insertConvo(Conversation convo){
        String name = "insertConvo";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, convo, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateConvo(int ConvoID, Conversation convo){
        String name = "updateConvo";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(ConvoID, convo, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer ConvoID, Conversation convo, String sqlFile){
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, convo.getConversation_ID());
        queryState.bindDouble(2, convo.getColour().getColour_ID());
        queryState.bindDouble(3, convo.getBridge().getBridge_ID());
        queryState.bindDouble(4, convo.getProfile().getProfile_ID());

        if (ConvoID != null){
            queryState.bindDouble(5, ConvoID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
