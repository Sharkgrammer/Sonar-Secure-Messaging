package com.shark.sonar.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shark.sonar.R;
import com.shark.sonar.controller.DbControl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbControl con = new DbControl(this);

        if (!con.databaseExists()){
            con.createTables();
        }

        //con.runDatabaseTest();
    }
}
