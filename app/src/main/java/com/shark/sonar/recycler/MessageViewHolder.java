package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shark.sonar.R;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblMessage;
    private LinearLayout layoutin, layouttop, layoutmessage;
    boolean fromUser = false;
    private Context context;

    public MessageViewHolder(View itemView, Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.layoutmessage = itemView.findViewById(R.id.linLayoutMessages);
        this.context = context;

        layouttop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromYou(fromUser = !fromUser);
            }
        });
    }

    // layouttop.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

    public void setImgPerson(int drawable) {
        imgPerson.setImageResource(drawable);
    }

    public void setTextMessage(String message) {
        lblMessage.setText(message);
    }

    public void addNewMessage(String message) {
        //TextView view = new ((Object) lblMessage).getClass();

        //AttributeSet att = lblMessage.a

        //view.setText(message);
        //layoutmessage.addView(view);
    }

    public void isFromYou(boolean fromUser) {
        int dir;
        if (fromUser){
            dir =  View.LAYOUT_DIRECTION_RTL;
        }else{
            dir = View.LAYOUT_DIRECTION_LTR;
        }
        layouttop.setLayoutDirection(dir);
        layoutin.setLayoutDirection(dir);
        layoutmessage.setLayoutDirection(dir);
        layouttop.refreshDrawableState();

    }




}