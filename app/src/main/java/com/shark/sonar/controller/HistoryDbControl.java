package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;
import com.shark.sonar.utility.ReadFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryDbControl extends DbControl {

    public HistoryDbControl(Context c){
        super(c);
    }

    public List<History> selectHistory(int convo_ID){
        List<History> result = new ArrayList<>();
        String name = "selectHistory";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");
            Cursor cursor;

            System.out.println(sqlFile + " : " + convo_ID);

            cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(convo_ID)});

            cursor.moveToFirst();

            History h;
            do{
                h = new History(context);

                h.setHistory_ID(cursor.getInt(0));
                h.setConversation_ID(cursor.getInt(1));

                int ID = cursor.getInt(5);
                ProfileDbControl profileDbControl = new ProfileDbControl(context);
                Profile userProf = profileDbControl.selectSingleProfile(ID);

                Message item = new Message(userProf.getIcon().getIcon_ID(), ID == 1, cursor.getString(2), cursor.getString(3), cursor.getString(6));
                h.setMessageObj(item);

                h.setEnd_date(cursor.getString(4));

                h.setUser_from(userProf);

                result.add(h);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteHistory(int ID, boolean convo){
        String name = "deleteHistory";

        try {
            ReadFile readFile = new ReadFile(context);

            String[] sqlFile = readFile.returnAssetAsString(name + ".sql").split(";");

            int mode = 0;

            if (convo){
                mode = 1;
            }

            db.execSQL(sqlFile[mode], new String[] {String.valueOf(ID)});


        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    public long insertHistory(History history){
        String name = "insertHistory";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            return insertUpdate(null, history, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return 0;
        }

    }


    public boolean updateHistory(int historyID, History history){
        String name = "updateHistory";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(historyID, history, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private long insertUpdate(Integer historyID, History history, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, history.getConversation_ID());
        queryState.bindString(2, history.getMessageObj().getMessage());
        queryState.bindString(3, history.getMessageObj().getTime());
        queryState.bindString(4, history.getEnd_date());
        queryState.bindDouble(5, history.getUser_from().getProfile_ID());
        queryState.bindString(6, history.getMessageObj().getImageMsg());

        if (historyID != null){
            queryState.bindDouble(7, historyID);
            return queryState.executeUpdateDelete();
        }else{
            return  queryState.executeInsert();
        }
    }



}
