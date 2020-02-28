package com.shark.sonar.recycler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.activity.MessageActivity;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.data.Conversation;

//REF https://www.javatpoint.com/android-recyclerview-list-example
class MainViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgPerson;
    private TextView lblPerson, lblMessage, lblID;
    private LinearLayout layoutin, layouttop;
    private Context context;
    private MainAdapter adapter;
    private int pos;
    private boolean textAdded = false, lineAdded = false;

    public MainViewHolder(View itemView, Context context, MainAdapter adapter) {
        super(itemView);

        this.imgPerson = itemView.findViewById(R.id.imgPerson);
        this.lblPerson = itemView.findViewById(R.id.lblPerson);
        this.lblMessage = itemView.findViewById(R.id.lblMessage);
        this.layouttop = itemView.findViewById(R.id.linLayoutTop);
        this.layoutin = itemView.findViewById(R.id.linLayoutInside);
        this.lblID = itemView.findViewById(R.id.lblID);
        this.context = context;
        this.adapter = adapter;
    }

    public void setImgPerson(int drawable) {
        imgPerson.setImageResource(drawable);
    }

    public void setTextPerson(String Person) {
        lblPerson.setText(Person);
    }

    public void setTextMessage(String Message) {
        lblMessage.setText(Message);
    }

    public void setID(int ID) {
        lblID.setText(String.valueOf(ID));
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setOnClick(Conversation c) {
        layoutin.setOnClickListener(view -> {

            Intent i = new Intent(context, MessageActivity.class);
            i.putExtra("ID", lblID.getText().toString());

            context.startActivity(i);
        });

        layoutin.setOnLongClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Conversation");
            builder.setMessage("'" + lblPerson.getText().toString() + "' will be deleted. Are you sure?\nThis cannot be undone");

            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {

                ConvoDbControl dbControl = new ConvoDbControl(context);
                boolean done = dbControl.deleteConvo(c);

                Toast.makeText(context, "Delete " + (done ? "Success" : "Failure"), Toast.LENGTH_LONG).show();
                if (done) adapter.ViewHolderUpdate(pos);
            });

            builder.setNegativeButton(android.R.string.no, null);
            builder.setIcon(imgPerson.getDrawable());
            builder.show();

            return true;
        });
    }

    public void addSpace() {
        if (!lineAdded) {
            View Child = LayoutInflater.from(context).inflate(R.layout.item_line, null);
            layouttop.addView(Child);

            lineAdded = true;
        }
    }

    public void addText(String text) {
        ;
        if (!textAdded) {
            View Child = LayoutInflater.from(context).inflate(R.layout.item_footer, null);

            TextView view = Child.findViewById(R.id.childTextView);
            view.setText(text);

            layouttop.addView(Child);

            textAdded = true;
        }
    }
}