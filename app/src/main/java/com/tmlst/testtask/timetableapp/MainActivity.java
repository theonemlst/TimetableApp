package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends Activity {

    public static final String CITYFROM = "FROM";
    public static final String CITYTO = "TO";

    public static final String STATION_TYPE = "stationType";
    public static final String STATION = "station";

    private static final String STATION_EQUAL = "stations are equal!";

    private static final int REQUEST_CODE = 1;
    private static final int DIALOG_DATE = 1;

    private static final DateFormat sdf =
            new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    private Model model;

    private TextView from;
    private TextView to;
    private TextView date;

    private Station stationFrom;
    private Station stationTo;
    private Calendar mCalendar;

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
                startChooseActivity(CITYFROM);
            }
        });
        if (stationFrom != null) from.setText(stationFrom.getStationTitle());

        to = findViewById(R.id.stationTo);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseActivity(CITYTO);
            }
        });
        if (stationTo != null) from.setText(stationTo.getStationTitle());

        mCalendar = Calendar.getInstance();
        date = findViewById(R.id.date);
        date.setText(sdf.format(mCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String stationType = null;
            Station station = null;
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                stationType = (String) bundle.get(STATION_TYPE);
                station = (Station) bundle.get(STATION);
            }
            if (stationType != null) {
                switch (stationType) {
                    case CITYFROM:
                        if (station != null && !station.equals(stationTo)) {
                            stationFrom = station;
                            from.setText(station.getStationTitle());
                        }
                        if (station != null && station.equals(stationTo)) {
                            Toast.makeText(this, STATION_EQUAL,
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case CITYTO:
                        if (station != null && !station.equals(stationFrom)) {
                            stationTo = station;
                            to.setText(station.getStationTitle());
                        }
                        if (station != null && station.equals(stationFrom)) {
                            Toast.makeText(this, STATION_EQUAL,
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
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onclick(View view) {
        showDialog(DIALOG_DATE);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            return  new DatePickerDialog(this, myCallBack,
                    mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH));
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(year, monthOfYear, dayOfMonth);
            date.setText(sdf.format(mCalendar.getTime()));
        }
    };
}
