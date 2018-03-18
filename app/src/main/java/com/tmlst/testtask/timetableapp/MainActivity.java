package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;


public class MainActivity extends Activity {

    public static final String STATION_TYPE = "stationType";

    private Model model;
    private TextView from;
    private TextView to;

    private Station stationFrom;
    private Station stationTo;

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
        if (stationFrom != null) from.setText(stationFrom.getStationTitle());

        to = findViewById(R.id.stationTo);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(JsonParser.CITYTO);
            }
        });
        if (stationTo != null) from.setText(stationTo.getStationTitle());
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
                        if (station != null && !station.equals(stationTo)) {
                            stationFrom = station;
                            from.setText(station.getStationTitle());
                        }
                        if (station != null && station.equals(stationTo)) {
                            Toast.makeText(this, "stations are equal!",
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case JsonParser.CITYTO:
                        if (station != null && !station.equals(stationFrom)) {
                            stationTo = station;
                            to.setText(station.getStationTitle());
                        }
                        if (station != null && station.equals(stationFrom)) {
                            Toast.makeText(this, "stations are equal!",
                                    Toast.LENGTH_LONG).show();
                        }
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
