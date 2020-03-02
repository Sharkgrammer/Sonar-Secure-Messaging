package com.shark.sonar.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.activity.MessageActivity;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.History;
import com.shark.sonar.data.Message;

import java.util.List;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<History> listData;
    private Conversation convo;
    private Context context;
    private MessageActivity act;
    private MessageViewHolder recentViewHolder;

    public MessageAdapter(Conversation convo, MessageActivity act) {
        this.convo = convo;
        this.listData = convo.getHistoryArrayList();
        this.context = act;
        this.act = act;
    }

    //REF https://stackoverflow.com/a/39678755/11480852
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, null, false);
        Child.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MessageViewHolder(Child, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int pos) {
        final Message data = listData.get(pos).getMessageObj();
        final History h = listData.get(pos);
        int ID = convo.getConversation_ID();

        System.out.println(pos + " " + data.getMessage());

        holder.setColour(convo.getColour());
        holder.onClick(h);
        holder.setPos(pos);

        if (data.getMessage().equals("")){
            holder.setImgView();
        }else{
            holder.setNormalView();
        }

        if (pos > 0) {
            Message prevData = listData.get(pos - 1).getMessageObj();

            if (prevData.isFromYou() == data.isFromYou()) {
                holder.addToCurrent(data, ID);
            } else {
                holder.makeNew(data, ID);
            }
        } else {
            holder.makeNew(data, ID);
        }

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

    public void add(History his) {
        listData.add(his);

        this.notifyItemInserted(listData.size() - 1);
    }

    public MessageViewHolder getRecentViewholder() {
        return recentViewHolder;
    }

    public void changeColours(Conversation convo){
        this.convo = convo;
        this.notifyDataSetChanged();
    }

    public void ViewHolderUpdate(int pos){
        listData.remove(pos);
        act.AdapterUpdate(listData);
        this.notifyDataSetChanged();
    }

    public void updateUserIcon(int iconID){
        Message tempMsg;
        for (History h : listData){
            tempMsg = h.getMessageObj();

            if (!tempMsg.isFromYou()){
                tempMsg.setImage(iconID);
                h.setMessageObj(tempMsg);
            }
        }

        this.notifyDataSetChanged();
    }

}