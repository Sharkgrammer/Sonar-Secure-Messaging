package com.shark.sonar.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.NetControl;
import com.shark.sonar.data.MainMessage;
import com.shark.sonar.recycler.MainAdapter;

import util.DataHolder;

public class MainActivity extends AppCompatActivity {

    private NetControl client;
    private DataHolder server;
    private String ID = "";
    private EditText txtIDto, txtMessage, txtIDfrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mainView = findViewById(R.id.imgPersonMain);
        mainView.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black));
        //DbControl con = new DbControl(this);

       // if (!con.databaseExists()){
         //   con.createTables();
      //  }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar);


        //REF https://www.javatpoint.com/android-recyclerview-list-example
        MainMessage[] data = new MainMessage[] {
                new MainMessage(R.drawable.ic_star_black, "Shark", "Howdy", "eh"),
                new MainMessage(R.drawable.ic_person_black, "Person", "Fuck you boi", "eh"),
                new MainMessage(R.drawable.ic_star_blue, "Kate", "I love sharks!", "eh"),
                new MainMessage(R.drawable.ic_person_red, "Not Kate", "I hate sharks", "eh"),
                new MainMessage(R.drawable.ic_person_orange, "Hmm", "This is one long fucking message, its wednesday my dudes", "eh"),
                new MainMessage(R.drawable.ic_star_orange, "This is one longer name then normal", "But whatever eh?", "eh"),
                new MainMessage(R.drawable.ic_star_red, "Pizza", "Nom da pizza", "eh"),
                new MainMessage(R.drawable.ic_person_purple, "Boop", "Boop the snoop", "eh"),
                new MainMessage(R.drawable.ic_star_yellow, "Shark", "Shark shark shark", "eh"),
                new MainMessage(R.drawable.ic_person_green, "Pizza boop shark", "Interesting...", "eh")
        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MainAdapter adapter = new MainAdapter(data, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void setID(View v){
        //if (!ID.equals("")) client.stop();

        ID = txtIDfrom.getText().toString();
        client = new NetControl(this, server, ID);

        client.sendAuthMessage();

        Toast.makeText(this, "ID set to " + ID, Toast.LENGTH_SHORT).show();
    }


    public void sendMessage(View v){
        String message = txtMessage.getText().toString(), to = txtIDto.getText().toString();
        client.sendMessage(message, to.getBytes());
        Toast.makeText(this, "Message: " + message + " sent to " + to, Toast.LENGTH_SHORT).show();
    }

}
