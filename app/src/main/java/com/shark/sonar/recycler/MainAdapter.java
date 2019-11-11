package com.shark.sonar.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.data.MainMessage;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private MainMessage[] listData;

    public MainAdapter(MainMessage[] listdata) {
        this.listData = listdata;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View Child = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, null);
        return new MainViewHolder(Child);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int pos) {
        final MainMessage data = listData[pos];

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
