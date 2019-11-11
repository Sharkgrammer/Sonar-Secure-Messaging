package com.shark.sonar.recycler;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shark.sonar.R;

class MainViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblPerson;
    private TextView lblMessage;
    private LinearLayout layout;


    public MainViewHolder(View itemView) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblPerson = itemView.findViewById(R.id.lblPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layout = itemView.findViewById(R.id.linLayout);
    }

    public void setImgPerson(int drawable){
        imgPerson.setImageResource(drawable);
    }

    public void setTextPerson(String Person){
        lblPerson.setText(Person);
    }

    public void setTextMessage(String Message){
        lblMessage.setText(Message);
    }

    public void setOnClick(View.OnClickListener listener){
        layout.setOnClickListener(listener);
    }
}