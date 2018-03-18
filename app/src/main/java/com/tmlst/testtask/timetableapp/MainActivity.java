package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tmlst.testtask.timetableapp.model.Model;


public class MainActivity extends Activity {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = Model.getInstance();
        ParseJsonTask parseJsonTask = new ParseJsonTask(this, model);
        parseJsonTask.execute();

        TextView from = findViewById(R.id.stationFrom);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(ParseJsonTask.CITYFROM);
            }
        });

        TextView to = findViewById(R.id.stationTo);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(ParseJsonTask.CITYTO);
            }
        });
    }

    public void startChooseActivity(String stationType) {
        Intent intent = new Intent(MainActivity.this, ChooseStationActivity.class);
        intent.putExtra("type", stationType);
        startActivity(intent);
    }
}
