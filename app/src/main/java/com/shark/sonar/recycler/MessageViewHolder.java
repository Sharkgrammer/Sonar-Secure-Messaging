package com.shark.sonar.recycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.controller.HistoryDbControl;
import com.shark.sonar.data.Colour;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;

import java.util.List;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson, imgMessage;
    private CardView imgCard;
    private TextView lblMessage, lblID;
    private LinearLayout layoutin, layouttop, layoutmessage, layoutwrapper;
    private Context context;
    private Colour colour;
    private boolean userFrom, isImage;
    private MessageAdapter adapter;
    private int LTR = View.LAYOUT_DIRECTION_LTR, RTL = View.LAYOUT_DIRECTION_RTL, pos;

    public MessageViewHolder(View itemView, final Context context, MessageAdapter adapter) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.imgCard = itemView.findViewById(R.id.imgCard);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.imgMessage = itemView.findViewById(R.id.imgMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.layoutmessage = itemView.findViewById(R.id.linLayoutMessages);
        this.layoutwrapper = itemView.findViewById(R.id.linLayoutMessageWrapper);
        this.lblID = itemView.findViewById(R.id.lblID);
        this.context = context;
        this.adapter = adapter;
        this.isImage = false;
    }

    public void setImgView(){
        lblMessage.setVisibility(View.GONE);
        imgMessage.setVisibility(View.VISIBLE);

        isImage = true;
    }

    public void setNormalView(){
        lblMessage.setVisibility(View.VISIBLE);
        imgMessage.setVisibility(View.GONE);

        isImage = false;
    }

    public void setImgPerson(int drawable) {
        System.out.println(drawable);

        imgPerson.setImageResource(drawable);

        imgPerson.setBackgroundColor(Color.parseColor(colour.getChat_Col_Background()));
    }

    public void setTextMessage(Message data, boolean addToCurrent) {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutin.getLayoutParams();

        int def = 3, expand = (def * def) * 2;

        if (addToCurrent) {

            layoutParams.topMargin = def;
            layoutParams.bottomMargin = def;

        }else{

            layoutParams.topMargin = expand;
            layoutParams.bottomMargin = def;

        }

        layoutin.setLayoutParams(layoutParams);

        if (isImage){
            //Uri uri = Uri.parse(data.getImageMsg());
            //imgMessage.setImageURI(uri);

            imgMessage.setImageBitmap(data.getImg(context));
        }else{
            lblMessage.setTextColor(Color.parseColor(colour.getText_Col()));
            lblMessage.setText(data.getMessage());
        }
    }

    public void onClick(History h){
        layoutmessage.setOnLongClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete " + lblMessage.getText().toString() + "?");

            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                System.out.println(h.getMessageObj().getMessage() + "   " + h.getHistory_ID());
                HistoryDbControl dbControl = new HistoryDbControl(context);
                boolean done = dbControl.deleteHistory(h.getHistory_ID(), false);

                Toast.makeText(context, "Delete " + (done ? "Success" : "Failure"), Toast.LENGTH_LONG).show();
                if (done) adapter.ViewHolderUpdate(pos);
            });

            builder.setNegativeButton(android.R.string.no, null);
            builder.show();

            return true;
        });
    }

    public void setID(int ID) {
        lblID.setText(String.valueOf(ID));
    }

    public void setPos(int pos) {
        this.pos = pos;
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