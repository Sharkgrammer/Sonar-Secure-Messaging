package com.shark.sonar.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shark.sonar.R;
import com.shark.sonar.activity.MainActivity;
import com.shark.sonar.controller.ConvoDbControl;
import com.shark.sonar.data.Conversation;
import com.shark.sonar.data.Message;
import com.shark.sonar.data.Profile;

import java.util.List;

//REF https://www.javatpoint.com/android-recyclerview-list-example
public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private List<Conversation> listData;
    private Context context;
    private MainActivity act;

    public MainAdapter(List<Conversation> listData, MainActivity act) {
        this.listData = listData;
        this.context = act;
        this.act = act;
    }

    //REF https://stackoverflow.com/a/39678755/11480852
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_message, null, false);
        Child.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new MainViewHolder(Child, context, this);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int pos) {
        final Conversation data = listData.get(pos);
        holder.setPos(pos);

        if (getItemCount() - 1 != pos){
            holder.addSpace();
        }else{
            holder.addText(context.getResources().getString(R.string.mainText));
        }

        Profile prof = data.getProfile();

        holder.setImgPerson(prof.getIcon().getIcon_ID());

        String messageStart = "Say hello to your friend!!";
        try{
            Message obj = data.getLatestMessage().getMessageObj();
            String msgObj = obj.getMessage();

            if (msgObj.equals("")){
                msgObj = "Sent an image";
            }

            if (obj.isFromYou()){
                messageStart = "You: ";
            }else{
                messageStart = "";
            }

            messageStart += msgObj;
        }catch (Exception e){
            Log.wtf("Error", e.toString());
        }

        holder.setTextMessage(messageStart);
        holder.setTextPerson(prof.getName());
        holder.setID(data.getConversation_ID());
        holder.setOnClick(data);

    }

    @Override
    public int getItemCount() {
        try{
            return listData.size();
        }catch(Exception e){
            return 0;
        }
    }

    public void ViewHolderUpdate(int pos){
        listData.remove(pos);
        this.notifyDataSetChanged();

        if (listData.size() == 0){
            act.updateMainDesc(true);
        }
    }

    public void updateList(List<Conversation> list){
        listData = list;
        this.notifyDataSetChanged();
    }

}
