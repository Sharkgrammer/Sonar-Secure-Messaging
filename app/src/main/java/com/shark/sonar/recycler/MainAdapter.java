package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.data.MainMessage;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private MainMessage[] listData;
    Context context;

    public MainAdapter(MainMessage[] listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    //REF https://stackoverflow.com/a/39678755/11480852
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, null, false);
        Child.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MainViewHolder(Child, context);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int pos) {
        final MainMessage data = listData[pos];

        if (getItemCount() - 1 != pos){
            holder.addSpace();
        }else{
            holder.addText(context.getResources().getString(R.string.mainText));
        }

        holder.setImgPerson(data.getImage());
        holder.setTextMessage(data.getMessage());
        holder.setTextPerson(data.getPerson());
        holder.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+ data.getPerson(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

}
