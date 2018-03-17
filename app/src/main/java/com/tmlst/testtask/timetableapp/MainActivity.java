package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Model model = new Model();
        new ParseJsonTask(this).execute();

//        FileHelper fileHelper = new FileHelper(this);
//        String jsonAsString = fileHelper.getJsonString();
//        Toast.makeText(this, jsonAsString.substring(3000000, 3000100),
//                    Toast.LENGTH_SHORT).show();


    }
}
