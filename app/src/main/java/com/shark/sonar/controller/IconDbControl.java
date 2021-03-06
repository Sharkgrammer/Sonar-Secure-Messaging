package com.shark.sonar.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.data.Icon;
import com.shark.sonar.utility.ReadFile;

import java.util.ArrayList;
import java.util.List;

public class IconDbControl extends DbControl {

    public IconDbControl(Context c){
        super(c);
    }

    public List<Icon> selectAllIcons(){
        return selectIcon(null);
    }

    public Icon selectSingleIcon(int Icon_ID){
        return selectIcon(Icon_ID).get(0);
    }

    private List<Icon> selectIcon(Integer icon_ID){
        List<Icon> result = new ArrayList<>();
        String name = "selectIcon", sqlFile;
        final int SELECT_ALL = 0, SELECT_SINGLE = 1;

        try {
            ReadFile readFile = new ReadFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name +".sql").split(";");
            Cursor cursor;

            if (icon_ID != null){

                sqlFile = sqlFileAll[SELECT_SINGLE];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(icon_ID)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }

            cursor.moveToFirst();

            Icon i;
            do{
                i = new Icon(cursor.getInt(0), context);

                result.add(i);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteIcon(int iconID){
        String name = "deleteIcon";

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

    public boolean insertIcon(Icon icon){
        String name = "insertIcon";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(null, icon, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }


    public boolean updateIcon(int iconID, Icon icon){
        String name = "updateIcon";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            insertUpdate(iconID, icon, sqlFile);

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    private void insertUpdate(Integer iconID, Icon icon, String sqlFile){
        SQLiteStatement queryState = db.compileStatement(sqlFile);

        queryState.bindDouble(1, icon.getIcon_ID());

        if (iconID != null){
            queryState.bindDouble(2, iconID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }



}
