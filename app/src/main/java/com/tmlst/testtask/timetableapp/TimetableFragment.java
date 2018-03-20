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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment implements JsonParser.OnParseListener{

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";

    private Station stationFrom;
    private Station stationTo;
    private Calendar mCalendar;

    private Context mContext;
    private ListviewAdapter listviewAdapter;
    ListView lv;

    ArrayList<ListViewData> dataList;

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

        lv = view.findViewById(R.id.list_view);

        dataList = new ArrayList<>();
        dataList.add(new ListViewData("", "Откуда", ""));
        dataList.add(new ListViewData("", "Куда", ""));
        dataList.add(new ListViewData("", "Дата", ""));
        dataList.add(new ListViewData("", "Сбросить", ""));

        listviewAdapter = new ListviewAdapter(mContext, dataList);
        lv.setAdapter(listviewAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listviewAdapter.isEnabled(position))
                    switch (position) {
                        case 0:
                            startFragment(CITYFROM);
                            break;
                        case 1:
                            startFragment(CITYTO);
                            break;
                        case 2:
                            DatePickerDialog datePicker = new DatePickerDialog(mContext,
                                    datePickerListener,
                                    mCalendar.get(Calendar.YEAR),
                                    mCalendar.get(Calendar.MONTH),
                                    mCalendar.get(Calendar.DAY_OF_MONTH));
                            datePicker.setCancelable(false);
                            datePicker.show();
                            break;
                        case 3:
                            stationFrom = null;
                            stationTo = null;
                            mCalendar = Calendar.getInstance();

                            dataList.get(0).setCountryTitle("Откуда");
                            dataList.get(0).setCityTitle("");
                            dataList.get(0).setStationTitle("");
                            dataList.get(1).setCountryTitle("Куда");
                            dataList.get(1).setCityTitle("");
                            dataList.get(1).setStationTitle("");
                            dataList.get(2).setCityTitle(sdf.format(mCalendar.getTime()));

                            State.getInstance().setStationFromId(-1);
                            State.getInstance().setStationToId(-1);
                            State.getInstance().setCalendar(mCalendar);

                            listviewAdapter.notifyDataSetChanged();
                            break;
                    }
            }

            private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    mCalendar.set(selectedYear, selectedMonth, selectedDay);
                    State.getInstance().setCalendar(mCalendar);
                    dataList.get(2).setCityTitle(sdf.format(mCalendar.getTime()));
                    listviewAdapter.notifyDataSetChanged();
                }
            };
        });

        mCalendar = State.getInstance().getCalendar();
        dataList.get(2).setCityTitle(sdf.format(mCalendar.getTime()));

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
        if (stationFrom != null){
            dataList.get(0).setCountryTitle(stationFrom.getStationTitle());
            dataList.get(0).setCityTitle(stationFrom.getCountryTitle());
            dataList.get(0).setStationTitle(stationFrom.getCityTitle());
        }

        stationTo = getStationById("TO", stationToId);
        if (stationTo != null) {
            dataList.get(1).setCountryTitle(stationTo.getStationTitle());
            dataList.get(1).setCityTitle(stationTo.getCountryTitle());
            dataList.get(1).setStationTitle(stationTo.getCityTitle());
        }

        dataList.get(2).setCityTitle(sdf.format(mCalendar.getTime()));
        listviewAdapter.notifyDataSetChanged();
        // ожидаем окончания парсинга
        if (State.getInstance().getModel().getCitiesTo() != null)
            listviewAdapter.setItemsEnabled(true);
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