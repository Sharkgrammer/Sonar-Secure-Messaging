package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Bridge;
import com.shark.sonar.utility.readFile;
import java.sql.SQLException;

public class BridgeDbControl extends DbControl {

    public BridgeDbControl(Context c){
        super(c);
    }

    public Bridge selectBridge(Integer convo_ID){
        Bridge result = null;
        String name = "selectBridge";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name +".sql");
            Cursor cursor;

            cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(convo_ID)});
            cursor.moveToFirst();

            Bridge b;
            b = new Bridge();

            b.setBridge_ID(cursor.getInt(0));
            b.setConversation_ID(cursor.getInt(1));

            result = b;
            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteBridge(int bridgeID){
        String name = "deleteBridge";

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

    public boolean insertBridge(Bridge bridge){
        String name = "insertBridge";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, bridge, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateBridge(int bridgeID, Bridge bridge){
        String name = "updateBridge";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(bridgeID, bridge, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer bridgeID, Bridge bridge, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, bridge.getBridge_ID());
        queryState.bindDouble(2, bridge.getConversation_ID());

        if (bridgeID != null){
            queryState.bindDouble(3, bridgeID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
