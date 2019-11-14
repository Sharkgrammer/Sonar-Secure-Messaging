package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.data.MainMessage;
import com.shark.sonar.data.Message;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private Message[] listData;
    private Context context;

    public MessageAdapter(Message[] listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    //REF https://stackoverflow.com/a/39678755/11480852
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        Child.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MessageViewHolder(Child, context);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int pos) {
        final Message data = listData[pos];

        holder.setImgPerson(data.getImage());
        holder.setTextMessage(data.getMessage());
        holder.isFromYou(data.isFromYou());
    }

    @Override
    public int getItemCount() {
        try {
            return listData.length;
        } catch (Exception e) {
            return 0;
        }
    }
}