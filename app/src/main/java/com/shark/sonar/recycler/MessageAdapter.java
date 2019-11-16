package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;

import java.util.List;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<History>  listData;
    private Conversation convo;
    private Context context;
    private MessageViewHolder recentViewHolder;

    public MessageAdapter(Conversation convo, Context context) {
        this.convo = convo;
        this.listData = convo.getHistoryArrayList();
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
        final Message data = listData.get(pos).getMessageObj();

        holder.setID(convo.getConversation_ID());
        holder.setImgPerson(data.getImage());
        holder.setTextMessage(data.getMessage());
        holder.isFromYou(data.isFromYou());

        recentViewHolder = holder;
    }

    @Override
    public int getItemCount() {
        try {
            return listData.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public MessageViewHolder getRecentViewholder(){
        return recentViewHolder;
    }
}