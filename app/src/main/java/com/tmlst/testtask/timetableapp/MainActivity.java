package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;


public class MainActivity extends Activity {

    public static final String STATION_TYPE = "stationType";

    private Model model;
    private TextView from;
    private TextView to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = Model.getInstance();
        JsonParser parseJsonTask = new JsonParser(this, model);
        parseJsonTask.execute();

        from = findViewById(R.id.stationFrom);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(JsonParser.CITYFROM);
            }
        });

        to = findViewById(R.id.stationTo);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(JsonParser.CITYTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String stationType = null;
            Station station = null;
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                stationType = (String) bundle.get(STATION_TYPE);
                station = (Station) bundle.get("station");
            }
            if (stationType != null) {
                switch (stationType) {
                    case JsonParser.CITYFROM:
                        if (station != null)
                            from.setText(station.getStationTitle());
                        break;
                    case JsonParser.CITYTO:
                        if (station != null)
                            to.setText(station.getStationTitle());
                        break;
                }
            }
        }
    }

    public void startChooseActivity(String stationType) {
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        intent.putExtra(STATION_TYPE, stationType);
        startActivityForResult(intent, 1);
    }
}
