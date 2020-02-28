package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Bridge;
import com.shark.sonar.data.Colour;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.ReadFile;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Conversation selectProfileConvo(byte[] Profile_ID){

        try{
            ProfileDbControl prof = new ProfileDbControl(context);

            Profile p = prof.selectSingleProfile(Profile_ID);

            return selectConversation(p.getProfile_ID(), null).get(0);
        }catch (Exception e){
            return null;
        }

    }

    public Conversation selectConvoByID(int Convo_ID){
        return selectConversation(null, Convo_ID).get(0);
    }

    private List<Conversation> selectConversation(Integer profile_ID, Integer convo_ID){
        List<Conversation> result = new ArrayList<>();
        String name = "selectConvo", sqlFile;
        final int SELECT_ALL = 0, SELECT_PROFILE = 1, SELECT_CONVO = 2;

        try {
            ReadFile readFile = new ReadFile(context);

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

            System.out.println(sqlFile);

            cursor.moveToFirst();

            Log.wtf("CONVO CURSOR", String.valueOf(cursor.getCount()));

            Conversation c;
            do{
                c = new Conversation(context);

                c.setConversation_ID(cursor.getInt(0));

                Colour col = new ColourDbControl(context).selectSingleColour(cursor.getInt(1));
                Bridge brid = new BridgeDbControl(context).selectBridge(cursor.getInt(2));
                Profile prof = new ProfileDbControl(context).selectSingleProfile(cursor.getInt(3));

                c.setColour(col);
                c.setBridge(brid);
                c.setProfile(prof);

                result.add(c);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString() + "  " + Arrays.toString(e.getStackTrace()));
        }

        return result;
    }

    public boolean deleteConvo(Conversation c){
        ProfileDbControl prof = new ProfileDbControl(context);
        prof.deleteProfile(c.getProfile().getProfile_ID());

        HistoryDbControl his = new HistoryDbControl(context);
        his.deleteHistory(c.getConversation_ID(), true);

        return deleteConvo(c.getConversation_ID());
    }

    public boolean deleteConvo(int ConvoID){
        String name = "deleteConvo";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            db.execSQL(sqlFile, new String[] {String.valueOf(ConvoID)});
        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    public boolean insertConvo(Conversation convo){
        String name = "insertConvo";

        try {
            ReadFile readFile = new ReadFile(context);

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
            ReadFile readFile = new ReadFile(context);

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
        int num = 1;

        queryState.bindDouble(num++, convo.getColour().getColour_ID());
        queryState.bindDouble(num++, convo.getBridge().getBridge_ID());
        queryState.bindDouble(num++, convo.getProfile().getProfile_ID());

        if (ConvoID != null){
            queryState.bindDouble(num, ConvoID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
