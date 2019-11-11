package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private Context context;
    private boolean textAdded = false, lineAdded = false;


    public MainViewHolder(View itemView, Context context) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblPerson = itemView.findViewById(R.id.lblPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layout = itemView.findViewById(R.id.linLayout);
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

    public void setOnClick(View.OnClickListener listener){
        layout.setOnClickListener(listener);
    }

    public void addSpace(){
        if (!lineAdded){
            View Child = LayoutInflater.from(context).inflate(R.layout.line, null);
            layout.addView(Child);

            lineAdded = true;
        }
    }

    public void addText(String text){ ;
        if (!textAdded){
            View Child = LayoutInflater.from(context).inflate(R.layout.footer, null);

            TextView view = Child.findViewById(R.id.childTextView);
            view.setText(text);

            layout.addView(Child);

            textAdded = true;
        }
    }
}