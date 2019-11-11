package com.shark.sonar.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shark.sonar.R;
import com.shark.sonar.controller.DbControl;
import com.shark.sonar.controller.NetControl;

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

        DbControl con = new DbControl(this);

        if (!con.databaseExists()){
            con.createTables();
        }

        System.out.println("shark test start");
        server = new DataHolder();
        server.setPort(6000);
        server.setIP("35.235.49.238");

        txtMessage = findViewById(R.id.txtMessage);
        txtIDfrom = findViewById(R.id.txtIDfrom);
        txtIDto = findViewById(R.id.txtIDto);
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
