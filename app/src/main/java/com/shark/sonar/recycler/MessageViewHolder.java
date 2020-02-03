package com.shark.sonar.recycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.data.Colour;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;

import java.util.List;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private CardView imgCard;
    private TextView lblMessage, lblID;
    private LinearLayout layoutin, layouttop, layoutmessage, layoutwrapper;
    private Context context;
    private Colour colour;
    private boolean userFrom;
    private int LTR = View.LAYOUT_DIRECTION_LTR, RTL = View.LAYOUT_DIRECTION_RTL;

    public MessageViewHolder(View itemView, final Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.imgCard = itemView.findViewById(R.id.imgCard);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.layoutmessage = itemView.findViewById(R.id.linLayoutMessages);
        this.layoutwrapper = itemView.findViewById(R.id.linLayoutMessageWrapper);
        this.lblID = itemView.findViewById(R.id.lblID);
        this.context = context;
    }

    public void setImgPerson(int drawable) {
        System.out.println(drawable);

        imgPerson.setImageResource(drawable);
    }

    public void setTextMessage(Message data, boolean addToCurrent) {

        if (addToCurrent) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutwrapper.getLayoutParams();
            layoutParams.topMargin = 3;
            layoutParams.bottomMargin = 3;

            layoutwrapper.setLayoutParams(layoutParams);
        }

        lblMessage.setTextColor(Color.parseColor(colour.getText_Col()));
        lblMessage.setText(data.getMessage());

    }

    public void setID(int ID) {
        lblID.setText(String.valueOf(ID));
    }

    public void isFromYou(boolean fromUser) {
        isFromYou(layoutin, layoutmessage, fromUser);
    }

    private void isFromYou(LinearLayout in, LinearLayout message, Boolean fromUser) {

        this.userFrom = fromUser;
        Drawable d;

        if (fromUser) {
            in.setLayoutDirection(RTL);

            d = context.getDrawable(R.drawable.outgoing_message);
            d.setColorFilter(Color.parseColor(colour.getChat_Col_From()), PorterDuff.Mode.MULTIPLY);

            message.setBackground(d);
        } else {
            in.setLayoutDirection(LTR);

            d = context.getDrawable(R.drawable.outgoing_message);
            d.setColorFilter(Color.parseColor(colour.getChat_Col_To()), PorterDuff.Mode.MULTIPLY);

            message.setBackground(d);
        }
    }

    private void setImgVis(int vis) {
        imgCard.setVisibility(vis);
    }

    public void addToCurrent(Message data, int ID) {
        setImgVis(View.INVISIBLE);
        setID(ID);
        setTextMessage(data, true);
        isFromYou(data.isFromYou());
    }

    public void makeNew(Message data, int ID) {
        setImgPerson(data.getImage());
        setID(ID);
        setTextMessage(data, false);
        isFromYou(data.isFromYou());
        setImgVis(View.VISIBLE);
    }

    public void setColour(Colour c){
        this.colour = c;
    }

}