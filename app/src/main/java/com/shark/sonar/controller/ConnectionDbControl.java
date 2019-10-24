package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Connection;
import com.shark.sonar.utility.readFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDbControl extends DbControl {

    public ConnectionDbControl(Context c){
        super(c);
    }

    private List<Connection> selectConnection(int bridge_ID){
        List<Connection> result = new ArrayList<>();
        String name = "selectConnection";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name +".sql");
            Cursor cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(bridge_ID)});

            cursor.moveToFirst();

            Connection c;
            do{
                c = new Connection();

                c.setConnection_ID(cursor.getInt(0));
                c.setBridge_ID(cursor.getInt(1));
                c.setServer_IP(cursor.getString(2));
                c.setKey(cursor.getBlob(3));
                c.setPosition(cursor.getInt(4));

                result.add(c);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteConnection(int connectionID){
        String name = "deleteConnection";

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

    public boolean insertConnection(Connection connection){
        String name = "insertConnection";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, connection, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateConnection(int connectionID, Connection connection){
        String name = "updateConnection";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(connectionID, connection, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer connectionID, Connection connection, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, connection.getConnection_ID());
        queryState.bindDouble(2, connection.getBridge_ID());
        queryState.bindString(3, connection.getServer_IP());
        queryState.bindBlob(4, connection.getKey());
        queryState.bindDouble(5, connection.getPosition());

        if (connectionID != null){
            queryState.bindDouble(7, connectionID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
