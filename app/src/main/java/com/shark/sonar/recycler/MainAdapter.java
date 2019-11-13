package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.data.MainMessage;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private MainMessage[] listData;
    private Context context;

    public MainAdapter(MainMessage[] listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    //REF https://stackoverflow.com/a/39678755/11480852
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_message, null, false);
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
        holder.setOnClick();

    }

    @Override
    public int getItemCount() {
        try{
            return listData.length;
        }catch(Exception e){
            return 0;
        }
    }

}
