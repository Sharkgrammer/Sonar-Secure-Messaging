package com.shark.sonar.data;

public class Colour {

    private int Colour_ID;
    private String Text_Col;
    private String Chat_Col_To;
    private String Chat_Col_From;
    private String Chat_Col_Background;
    private String Text_Background_Col;
    private String Col_Name;

    public int getColour_ID() {
        return Colour_ID;
    }

    public void setColour_ID(int colour_ID) {
        Colour_ID = colour_ID;
    }

    public String getText_Col() {
        return Text_Col;
    }

    public void setText_Col(String text_Col) {
        Text_Col = text_Col;
    }

    public String getChat_Col_To() {
        return Chat_Col_To;
    }

    public void setChat_Col_To(String chat_Col_To) {
        Chat_Col_To = chat_Col_To;
    }

    public String getChat_Col_From() {
        return Chat_Col_From;
    }

    public void setChat_Col_From(String chat_Col_From) {
        Chat_Col_From = chat_Col_From;
    }

    public String getChat_Col_Background() {
        return Chat_Col_Background;
    }

    public void setChat_Col_Background(String chat_Col_Background) {
        Chat_Col_Background = chat_Col_Background;
    }

    public String getText_Background_Col() {
        return Text_Background_Col;
    }

    public void setText_Background_Col(String text_Background_Col) {
        Text_Background_Col = text_Background_Col;
    }

    public String getCol_Name() {
        return Col_Name;
    }

    public void setCol_Name(String col_Name) {
        Col_Name = col_Name;
    }
}
