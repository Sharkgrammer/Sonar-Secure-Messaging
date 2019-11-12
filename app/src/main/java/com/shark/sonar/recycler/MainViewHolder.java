package com.shark.sonar.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.activity.MessageActivity;

//REF https://www.javatpoint.com/android-recyclerview-list-example
class MainViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblPerson;
    private TextView lblMessage;
    private LinearLayout layoutin, layouttop;
    private Context context;
    private boolean textAdded = false, lineAdded = false;

    public MainViewHolder(View itemView, Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblPerson = itemView.findViewById(R.id.lblPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.context = context;
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

    public void setOnClick(){
        layoutin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MessageActivity.class));
            }
        });
    }

    public void addSpace(){
        if (!lineAdded){
            View Child = LayoutInflater.from(context).inflate(R.layout.item_line, null);
            layouttop.addView(Child);

            lineAdded = true;
        }
    }

    public void addText(String text){ ;
        if (!textAdded){
            View Child = LayoutInflater.from(context).inflate(R.layout.item_footer, null);

            TextView view = Child.findViewById(R.id.childTextView);
            view.setText(text);

            layouttop.addView(Child);

            textAdded = true;
        }
    }
}