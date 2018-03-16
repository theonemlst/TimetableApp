package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParseJsonTask(this).execute();

//        FileHelper fileHelper = new FileHelper(this);
//        String jsonAsString = fileHelper.getJsonString();
//        Toast.makeText(this, jsonAsString.substring(3000000, 3000100),
//                    Toast.LENGTH_SHORT).show();


    }
}
