package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;
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

            String sqlFile = readFile.returnAssetAsString(name +".sql");
            Cursor cursor;

            cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(convo_ID)});

            cursor.moveToFirst();

            History h;
            do{
                h = new History();

                h.setHistory_ID(cursor.getInt(0));
                h.setConversation_ID(cursor.getInt(1));

                int ID = cursor.getInt(5);
                Message item = new Message(0, ID == 1, cursor.getString(2), cursor.getString(3));
                h.setMessageObj(item);

                h.setEnd_date(cursor.getString(4));
                h.setUser_from(ID);

                result.add(h);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteHistory(int historyID){
        String name = "deleteHistory";

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

    public boolean insertHistory(History history){
        String name = "insertHistory";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, history, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
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

    private void insertUpdate(Integer historyID, History history, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, history.getHistory_ID());
        queryState.bindDouble(2, history.getConversation_ID());
        queryState.bindString(3, history.getMessageObj().getMessage());
        queryState.bindString(4, history.getMessageObj().getTime());
        queryState.bindString(5, history.getEnd_date());
        queryState.bindDouble(6, history.getUser_from().getProfile_ID());

        if (historyID != null){
            queryState.bindDouble(7, historyID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
