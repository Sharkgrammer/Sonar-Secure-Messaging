package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Profile;

import java.util.List;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private List<Conversation> listData;
    private Context context;

    public MainAdapter(List<Conversation> listData, Context context) {
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
        final Conversation data = listData.get(pos);

        if (getItemCount() - 1 != pos){
            holder.addSpace();
        }else{
            holder.addText(context.getResources().getString(R.string.mainText));
        }

        Profile prof = data.getProfile();

        holder.setImgPerson(prof.getIcon().getIcon_ID());
        holder.setTextMessage(data.getLatestMessage().getMessageObj().getMessage());
        holder.setTextPerson(prof.getName());
        holder.setID(data.getConversation_ID());
        holder.setOnClick();

    }

    @Override
    public int getItemCount() {
        try{
            return listData.size();
        }catch(Exception e){
            return 0;
        }
    }

}
