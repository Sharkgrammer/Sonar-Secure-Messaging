package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;

import java.util.List;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblMessage, lblID;
    private LinearLayout layoutin, layouttop, layoutmessage, layoutwrapper;
    private Context context;
    private boolean userFrom;
    private int LTR = View.LAYOUT_DIRECTION_LTR, RTL = View.LAYOUT_DIRECTION_RTL;

    public MessageViewHolder(View itemView, final Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
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

    public void setTextMessage(String message) {
        lblMessage.setText(message);
        //lblMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));    }
    }

    public void setID(int ID) {
        lblID.setText(String.valueOf(ID));
    }

    public void addNewMessage(String message) {
        View child = LayoutInflater.from(context).inflate(R.layout.item_single_message, null);
        TextView messageView = child.findViewById(R.id.lblMessage);
        LinearLayout lay_message = child.findViewById(R.id.linLayoutMessages);
        isFromYou(layoutin, lay_message, userFrom);

        messageView.setText(message);

        layoutwrapper.addView(child);
    }

    public void isFromYou(boolean fromUser) {
        isFromYou(layoutin, layoutmessage, fromUser);
    }

    private void isFromYou(LinearLayout in, LinearLayout message, Boolean fromUser) {

        this.userFrom = fromUser;

        if (fromUser){
            in.setLayoutDirection(RTL);
            message.setBackground(context.getDrawable(R.drawable.outgoing_message));
        }else{
            in.setLayoutDirection(LTR);
            message.setBackground(context.getDrawable(R.drawable.incoming_message));
        }
    }

}