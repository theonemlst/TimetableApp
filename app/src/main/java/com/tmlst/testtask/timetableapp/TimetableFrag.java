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

import static com.tmlst.testtask.timetableapp.MainActivity.TO;
import static com.tmlst.testtask.timetableapp.MainActivity.FROM;
import static com.tmlst.testtask.timetableapp.MainActivity.NULL_STATION_INDEX;

/**
 *   Фрагмент расписания
 */

public class TimetableFrag extends Fragment implements JsonParser.OnParseListener{

    private static final int FROM_INDEX = 0;
    private static final int TO_INDEX = 1;
    private static final int DATE_INDEX = 2;
    private static final int CANCEL_INDEX = 3;

    private static final String SIMPLE_DATE_FORMAT_PATTERN = "dd.MM.yyyy";

    private Context mContext;
    private MenuAdapter menuAdapter;

    private String fromString;
    private String toString;
    private String dateString;
    private String resetString;
    private String emptyString;

    private Station stationFrom;
    private Station stationTo;
    private Calendar mCalendar;

    private AppState appState;

//    Задаем формат вывода даты
    private static final DateFormat sdf =
            new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN, Locale.US);

    public TimetableFrag() {}

    public void setmContext(Context mContext) {

        this.mContext = mContext;

        fromString = mContext.getString(R.string.from_string);
        toString = mContext.getString(R.string.to_string);
        dateString = mContext.getString(R.string.date_string);
        resetString = mContext.getString(R.string.reset_string);
        emptyString = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
//        Задаем заголовок в панели действий
        ((Activity) mContext).setTitle(R.string.app_name);

        appState = AppState.getInstance();
        menuAdapter = new MenuAdapter(mContext, initListViewData());
        ListView listView = view.findViewById(R.id.list_view);
//        Устанавливаем адаптер меню и обработчик нажатий на пункты меню
        listView.setAdapter(menuAdapter);
        listView.setOnItemClickListener(menuClickListener);

        mCalendar = appState.getCalendar();
        getMenuItemData(DATE_INDEX).setMiddleText(sdf.format(mCalendar.getTime()));
//        Регистрируемся как слушатель события окончания парсинга
        JsonParser.setOnParseListener(this);
        loadStations();

        return view;
    }

//    Функция создания и добавления во фрагмент-менеджер фрагмена выбора станции
    private void startFragment(String stationType) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        ChooseFrag chooseFrag = new ChooseFrag();
        chooseFrag.setmContext(mContext);
        chooseFrag.setStationsType(stationType);
        fragmentTransaction.replace(R.id.main_container, chooseFrag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onComplete() {
        loadStations();
    }

//    Функция установки выбранных станции из модели, задает
//    станции при инициализации фрагмента, либо по окончании парсинга
    private void loadStations() {

        long stationFromId = appState.getStationFromId();
        long stationToId = appState.getStationToId();

        stationFrom = getStationById(FROM, stationFromId);
        if (stationFrom != null){
            setMenuItemData(FROM_INDEX, stationFrom);
        }

        stationTo = getStationById(TO, stationToId);
        if (stationTo != null) {
            setMenuItemData(TO_INDEX, stationTo);
        }

        getMenuItemData(DATE_INDEX).setMiddleText(sdf.format(mCalendar.getTime()));
        menuAdapter.notifyDataSetChanged();
        // Ожидаем окончания парсинга и Разрешаем нажатия на первые два пункта меню
        if (appState.getModel().getCitiesTo() != null)
            menuAdapter.setItemsEnabled();
    }

//    Обновляет внешний вид пункта меню
    private void setMenuItemData(int index, Station station) {
        getMenuItemData(index).setBigText(station.getStationTitle());
        getMenuItemData(index).setMiddleText(station.getCountryTitle());
        getMenuItemData(index).setSmallText(station.getCityTitle());
    }

//    Получаем станцию по id
    private Station getStationById(String stationType, long id) {
        List<City> cities = new ArrayList<>();
        Model model = appState.getModel();
        if (model.getCitiesFrom() != null && FROM.equals(stationType)) {
            cities = model.getCitiesFrom();
        }
        if (model.getCitiesTo() != null && TO.equals(stationType)) {
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

//    Возвращает объект данных пункта меню
    private MenuItemData getMenuItemData(int index) {
        return (MenuItemData) menuAdapter.getItem(index);
    }

//    Инициализация данных элементов меню
    private ArrayList<MenuItemData> initListViewData() {

        ArrayList<MenuItemData> list = new ArrayList<>();

        list.add(new MenuItemData(emptyString, fromString, emptyString, R.drawable.ic_action_from));
        list.add(new MenuItemData(emptyString, toString, emptyString, R.drawable.ic_action_to));
        list.add(new MenuItemData(emptyString, dateString, emptyString, R.drawable.ic_action_date));
        list.add(new MenuItemData(emptyString, resetString, emptyString, R.drawable.ic_action_cancel));

        return list;
    }

//    Обработчик нажатий на пункты меню
    private AdapterView.OnItemClickListener menuClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (menuAdapter.isEnabled(position))
                switch (position) {
                    case FROM_INDEX:
//                        Запуск фрагмента выбора станции
                        startFragment(FROM);
                        break;
                    case TO_INDEX:
//                        Запуск фрагмента выбора станции
                        startFragment(TO);
                        break;
                    case DATE_INDEX:
//                        Запуск DatePickerDialog
                        DatePickerDialog datePicker = new DatePickerDialog(mContext,
                                datePickerListener,
                                mCalendar.get(Calendar.YEAR),
                                mCalendar.get(Calendar.MONTH),
                                mCalendar.get(Calendar.DAY_OF_MONTH));
                        datePicker.setCancelable(false);
                        datePicker.show();
                        break;
                    case CANCEL_INDEX:
//                        Сброс параметров
                        stationFrom = null;
                        stationTo = null;
                        mCalendar = Calendar.getInstance();

                        getMenuItemData(FROM_INDEX).setBigText(fromString);
                        getMenuItemData(FROM_INDEX).setMiddleText(emptyString);
                        getMenuItemData(FROM_INDEX).setSmallText(emptyString);
                        getMenuItemData(TO_INDEX).setBigText(toString);
                        getMenuItemData(TO_INDEX).setMiddleText(emptyString);
                        getMenuItemData(TO_INDEX).setSmallText(emptyString);
                        getMenuItemData(DATE_INDEX).setMiddleText(sdf.format(mCalendar.getTime()));

                        appState.setStationFromId(NULL_STATION_INDEX);
                        appState.setStationToId(NULL_STATION_INDEX);
                        appState.setCalendar(mCalendar);

                        menuAdapter.notifyDataSetChanged();
                        break;
                }
        }
//        Обрабатываем выбранную дату
        private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                mCalendar.set(selectedYear, selectedMonth, selectedDay);
                appState.setCalendar(mCalendar);
                getMenuItemData(DATE_INDEX).setMiddleText(sdf.format(mCalendar.getTime()));
                menuAdapter.notifyDataSetChanged();
            }
        };
    };
}