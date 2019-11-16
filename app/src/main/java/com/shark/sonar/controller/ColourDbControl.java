package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Colour;
import com.shark.sonar.utility.readFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColourDbControl extends DbControl {

    public ColourDbControl(Context c){
        super(c);
    }

    public List<Colour> selectAllcolour(){
        return selectColour(null);
    }

    public Colour selectSingleColour(int Colour_ID){
        try{
            return selectColour(Colour_ID).get(0);
        }catch (Exception e){
            return null;
        }
    }

    private List<Colour> selectColour(Integer colour_ID){
        List<Colour> result = new ArrayList<>();
        String name = "selectColour", sqlFile;
        final int SELECT_ALL = 0, SELECT_SINGLE = 1;

        try {
            readFile readFile = new readFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name +".sql").split(";");
            Cursor cursor;

            if (colour_ID != null){

                sqlFile = sqlFileAll[SELECT_SINGLE];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(colour_ID)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }

            cursor.moveToFirst();

            Colour c;
            do{
                c = new Colour();

                c.setColour_ID(cursor.getInt(0));
                c.setChat_Col_To(cursor.getString(1));
                c.setChat_Col_From(cursor.getString(2));
                c.setText_Col(cursor.getString(3));
                c.setChat_Col_Background(cursor.getString(4));
                c.setChat_Col_Accent(cursor.getString(5));

                result.add(c);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteColour(int colourID){
        String name = "deleteColour";

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

    public boolean insertColour(Colour colour){
        String name = "insertColour";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, colour, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateColour(int colourID, Colour colour){
        String name = "updateColour";

        try {
            readFile readFile = new readFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(colourID, colour, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer colourID, Colour colour, String sqlFile) throws SQLException {
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, colour.getColour_ID());
        queryState.bindString(2, colour.getChat_Col_To());
        queryState.bindString(3, colour.getChat_Col_From());
        queryState.bindString(4, colour.getText_Col());
        queryState.bindString(5, colour.getChat_Col_Background());
        queryState.bindString(6, colour.getChat_Col_Accent());

        if (colourID != null){
            queryState.bindDouble(7, colourID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
