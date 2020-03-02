package com.shark.sonar.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.shark.sonar.R;
import com.shark.sonar.data.Colour;
import com.shark.sonar.utility.ReadFile;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ColourDbControl extends DbControl {

    public ColourDbControl(Context c){
        super(c);
    }

    public List<Colour> selectAllColours(){
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
            ReadFile readFile = new ReadFile(context);

            String[] sqlFileAll = readFile.returnAssetAsString(name +".sql").split(";");
            Cursor cursor;

            if (colour_ID != null){

                System.out.println("SELECT SINGLE COLOUR: " + colour_ID);

                sqlFile = sqlFileAll[SELECT_SINGLE];
                cursor = db.rawQuery(sqlFile, new String[] {String.valueOf(colour_ID)});

            }else{

                sqlFile = sqlFileAll[SELECT_ALL];
                cursor = db.rawQuery(sqlFile, null);

            }


            System.out.println("SELECT COLOURS: " + sqlFile);

            cursor.moveToFirst();

            Colour c;
            do{
                c = new Colour();

                c.setColour_ID(cursor.getInt(0));
                c.setChat_Col_To(cursor.getString(1));
                c.setChat_Col_From(cursor.getString(2));
                c.setText_Col(cursor.getString(3));
                c.setChat_Col_Background(cursor.getString(4));
                c.setText_Background_Col(cursor.getString(5));
                c.setCol_Name(cursor.getString(6));
                c.setPrimary_Col(cursor.getString(7));
                c.setPrimary_Col_Dark(cursor.getString(8));
                c.setHint_Col(cursor.getString(9));

                result.add(c);

            }while(cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
        }

        return result;
    }

    public boolean deleteColour(int colour_ID){
        String name = "deleteColour";

        try {
            ReadFile readFile = new ReadFile(context);

            String sqlFile = readFile.returnAssetAsString(name + ".sql");

            db.execSQL(sqlFile, new String[] {String.valueOf(colour_ID)});
        } catch (Exception e) {
            Log.wtf("Error in " + name, e.toString());
            return false;
        }

        return true;
    }

    public boolean insertColour(Colour colour){
        String name = "insertColour";

        try {
            ReadFile readFile = new ReadFile(context);

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
            ReadFile readFile = new ReadFile(context);

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

        queryState.bindString(1, colour.getChat_Col_To());
        queryState.bindString(2, colour.getChat_Col_From());
        queryState.bindString(3, colour.getText_Col());
        queryState.bindString(4, colour.getChat_Col_Background());
        queryState.bindString(5, colour.getText_Background_Col());
        queryState.bindString(6, colour.getCol_Name());
        queryState.bindString(7, colour.getPrimary_Col());
        queryState.bindString(8, colour.getPrimary_Col_Dark());
        queryState.bindString(9, colour.getHint_Col());

        if (colourID != null){
            queryState.bindDouble(10, colourID);
            queryState.executeUpdateDelete();
        }else{
            queryState.executeInsert();
        }
    }

    @SuppressLint("ResourceType")
    public void makeSampleColours(Boolean reset){

        if (reset){
            for (Colour c : selectAllColours()){
                deleteColour(c.getColour_ID());
            }
        }

        List<Colour> colours = new ArrayList<>();
        int ID = 1;
        Colour colour = new Colour();

        Resources res = context.getResources();

        colour.setCol_Name("Default");
        colour.setChat_Col_Background(res.getString(R.color.background));
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From(res.getString(R.color.colorAccent));
        colour.setChat_Col_To(res.getString(R.color.colorPrimary));
        colour.setColour_ID(ID++);
        colour.setText_Col(res.getString(R.color.colorTextLight));
        colour.setPrimary_Col(res.getString(R.color.colorPrimary));
        colour.setPrimary_Col_Dark(res.getString(R.color.colorPrimaryDark));
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Original");
        colour.setChat_Col_Background(res.getString(R.color.background));
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#EB5600");
        colour.setChat_Col_To("#008080");
        colour.setColour_ID(ID++);
        colour.setText_Col("#ffffff");
        colour.setPrimary_Col("#008080");
        colour.setPrimary_Col_Dark("#006363");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Sunshine");
        colour.setChat_Col_Background(res.getString(R.color.background));
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#DAA520");
        colour.setChat_Col_To("#CD853F");
        colour.setText_Col("#f4f4f4");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#DAA520");
        colour.setPrimary_Col_Dark("#BF911D");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Hacker");
        colour.setChat_Col_Background("#111111");
        colour.setText_Background_Col("#20C20E");
        colour.setChat_Col_From("#111111");
        colour.setChat_Col_To("#111111");
        colour.setText_Col("#20C20E");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#111111");
        colour.setPrimary_Col_Dark("#111111");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Water");
        colour.setChat_Col_Background("#EEFFFF");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#00b7d4");
        colour.setChat_Col_To("#008BA1");
        colour.setText_Col("#ffffff");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#00B7D4");
        colour.setPrimary_Col_Dark("#008BA1");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Futuristic");
        colour.setChat_Col_Background("#D1D1D1");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#CA8D8D");
        colour.setChat_Col_To("#9E6F6F");
        colour.setText_Col("#ffffff");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#CA8D8D");
        colour.setPrimary_Col_Dark("#9E6F6F");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Cozy");
        colour.setChat_Col_Background(res.getString(R.color.background));
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#990099");
        colour.setChat_Col_To("#660066");
        colour.setText_Col("#ffffff");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#990099");
        colour.setPrimary_Col_Dark("#660066");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Arctic");
        colour.setChat_Col_Background("#f6fcfc");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#d3e7f3");
        colour.setChat_Col_To("#b2d8ed");
        colour.setText_Col("#000000");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#85C4FF");
        colour.setPrimary_Col_Dark("#85C4FF");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("WhatsApp");
        colour.setChat_Col_Background("#ECE5DD");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#25D366");
        colour.setChat_Col_To("#FFFFFF");
        colour.setText_Col("#000000");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#128C7E");
        colour.setPrimary_Col_Dark("#075E54");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("IOS");
        colour.setChat_Col_Background("#F2F2F7");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#007AFF");
        colour.setChat_Col_To("#8E8E93");
        colour.setText_Col("#ffffff");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#8E8E93");
        colour.setPrimary_Col_Dark("#8E8E93");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        colour.setCol_Name("Android");
        colour.setChat_Col_Background("#ECE5DD");
        colour.setText_Background_Col("#000000");
        colour.setChat_Col_From("#25D366");
        colour.setChat_Col_To("#FFFFFF");
        colour.setText_Col("#000000");
        colour.setColour_ID(ID++);
        colour.setPrimary_Col("#128C7E");
        colour.setPrimary_Col_Dark("#075E54");
        colour.setHint_Col("#808080");

        colours.add(colour);
        colour = new Colour();

        System.out.println(colours.size());

        for (Colour c : colours){
            System.out.println(c.getCol_Name() + ":" + this.insertColour(c));
        }


    }



}
