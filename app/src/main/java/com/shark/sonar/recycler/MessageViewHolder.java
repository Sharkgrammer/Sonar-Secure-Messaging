package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shark.sonar.R;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblMessage;
    private LinearLayout layoutin, layouttop, layoutmessage, layoutwrapper;
    private Context context;
    private boolean userFrom;
    private int LTR = View.LAYOUT_DIRECTION_LTR, RTL = View.LAYOUT_DIRECTION_RTL;

    public MessageViewHolder(View itemView, Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.layoutmessage = itemView.findViewById(R.id.linLayoutMessages);
        this.layoutwrapper = itemView.findViewById(R.id.linLayoutMessageWrapper);
        this.context = context;

        layouttop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMessage("I also said this! And through speaking like this i made this message much larger then it was");
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