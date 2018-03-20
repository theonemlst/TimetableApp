package com.tmlst.testtask.timetableapp;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tmlst.testtask.timetableapp.model.City;
import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.tmlst.testtask.timetableapp.MainActivity.CITYFROM;
import static com.tmlst.testtask.timetableapp.MainActivity.CITYTO;
import static com.tmlst.testtask.timetableapp.MainActivity.STATION_FROM_ID;
import static com.tmlst.testtask.timetableapp.MainActivity.STATION_TO_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment  implements JsonParser.OnParseListener{

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";

    private TextView from;
    private TextView to;
    private TextView date;

    private Station stationFrom;
    private Station stationTo;
    private Calendar mCalendar;

    private Context mContext;

    private static final DateFormat sdf =
            new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    public TimetableFragment() {}

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        ((Activity) mContext).setTitle(R.string.app_name);

        from = view.findViewById(R.id.stationFrom);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(CITYFROM);
            }
        });

        to = view.findViewById(R.id.stationTo);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(CITYTO);
            }
        });

        mCalendar = State.getInstance().getCalendar();
        date = view.findViewById(R.id.date);
        date.setText(sdf.format(mCalendar.getTime()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(mContext,
                        datePickerListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.show();
            }

            private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    mCalendar.set(selectedYear, selectedMonth, selectedDay);
                    State.getInstance().setCalendar(mCalendar);
                    date.setText(sdf.format(mCalendar.getTime()));
                }
            };
        });

        JsonParser.setOnParseListener(this);
        loadStations();

        return view;
    }

    private void startFragment(String stationType) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        ChooseFragment chooseFragment = new ChooseFragment();
        chooseFragment.setmContext(mContext);
        chooseFragment.setStationsType(stationType);
        fragmentTransaction.replace(R.id.main_container, chooseFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onComplete() {
        loadStations();
    }

    private void loadStations() {

        long stationFromId = State.getInstance().getStationFromId();
        long stationToId = State.getInstance().getStationToId();

        stationFrom = getStationById("FROM", stationFromId);
        if (stationFrom != null)
            from.setText(stationFrom.getStationTitle());

        stationTo = getStationById("TO", stationToId);
        if (stationTo != null)
            to.setText(stationTo.getStationTitle());

        date.setText(sdf.format(mCalendar.getTime()));

    }

    private Station getStationById(String stationType, long id) {
        List<City> cities = new ArrayList<>();
        Model model = State.getInstance().getModel();
        if (model.getCitiesFrom() != null && "FROM".equals(stationType)) {
            cities = model.getCitiesFrom();
        }
        if (model.getCitiesTo() != null && "TO".equals(stationType)) {
            cities = model.getCitiesTo();
        }

        for (City city: cities) {
            List<Station> stations = city.getStations();
            for(Station station: stations) {
                if (station.getStationId() == id) {
                    return station;
                }
            }
        }

        return null;
    }
}