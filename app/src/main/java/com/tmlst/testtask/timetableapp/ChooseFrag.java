package com.tmlst.testtask.timetableapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.tmlst.testtask.timetableapp.model.Station;

import static com.tmlst.testtask.timetableapp.MainActivity.FROM;
import static com.tmlst.testtask.timetableapp.MainActivity.TO;

/**
 *    Фрагмент экрана выбора станции
 */

public class ChooseFrag extends Fragment implements SearchView.OnQueryTextListener, JsonParser.OnParseListener {

    private ListAdapter adapter;
    private ExpandableListView expandableListView;
    private String stationsType;
    SearchView editSearch;

    private Context mContext;
    private AppState appState;

    private int lastExpandedPosition = -1;

    public ChooseFrag() {}

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setStationsType(String stationsType) {
        this.stationsType = stationsType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose, container, false);

        appState = AppState.getInstance();

        expandableListView = view.findViewById(R.id.list_view);

//        Обраюотчик долгого нажатия на дочерний элемент списка,
//                выводит подробную информацию о станции
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Station station = appState.getModel().getCitiesFrom().
                            get(groupPosition).getStations().get(childPosition);
                    String stationInfo = getString(R.string.detail_info_country_label) +
                            station.getCountryTitle() + "\n" +
                            getString(R.string.detail_info_city_label) + station.getCityTitle() + "\n" +
                            getString(R.string.detail_info_region_label) + station.getRegionTitle() + "\n" +
                            getString(R.string.detail_info_district_label) + station.getDistrictTitle();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(station.getStationTitle())
                            .setMessage(stationInfo)
                            .setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;
                }

                return false;
            }
        });

//        Обработчик нажатия на дочерний элемент
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Если выбранная станция не совпадает с другой выбранной - запоминаем ее,
//                  иначе показываем Toast
                switch (stationsType) {
                    case FROM:
                        long newStationFromId = adapter.getStationId(groupPosition, childPosition);
                        if (appState.getStationToId() != newStationFromId) {
                            appState.setStationFromId(newStationFromId);
                        } else {
                            Toast.makeText(mContext, getString(R.string.stations_are_equal_toast_text),
                                    Toast.LENGTH_LONG).show();
                        }
                        break;
                    case TO:
                        long newStationToId = adapter.getStationId(groupPosition, childPosition);
                        if (appState.getStationFromId() != newStationToId) {
                            appState.setStationToId(newStationToId);
                        } else {
                            Toast.makeText(mContext, getString(R.string.stations_are_equal_toast_text), Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                // скрываем клавиатуру
                hideKeyBoard();
                // Возвращаем предыдущий фрагмент
                ((Activity) mContext).onBackPressed();

                return false;
            }
        });

//        При раскрытии новой группы сворачиваем предыдущую и прячем клавиатуру
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
                hideKeyBoard();
            }
        });

        ((Activity) mContext).setTitle(R.string.app_name);
//        Регистрируем объект класса слушателем события окончания парсинга
        JsonParser.setOnParseListener(this);
        setStationsAdapter();

        editSearch = view.findViewById(R.id.search);

//        Устанавливаем слушателя поисковых запросов SearchView
        editSearch.setOnQueryTextListener(this);

//        При установке фокуса в SearchView закрываем раскрытую группу
        editSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                expandableListView.collapseGroup(lastExpandedPosition);
            }
        });

        return view;
    }

//    Прячет клавиатуру
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }

    @Override
    public void onComplete() {
        setStationsAdapter();
    }

//    Функция установки адаптера для ExpandableListView, вызывается при
//            инициализации фрагмента и в методе onComplete().
//    Если парсинг не завершен в момент инициализации фрагмента -
//    устанавливает его по завершении парсинга в методе onComplete(),
//    иначе - устанавливает сразу, метод onComplete() вызван не будет.
    private void setStationsAdapter() {

        switch (stationsType) {
            case FROM:
                    adapter = ListAdapter.getAdapterFrom();
                break;
            case TO:
                    adapter = ListAdapter.getAdapterTo();
                break;
        }

        if (adapter != null) {
            expandableListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
//        Cбраcываем фильтр
            adapter.getFilter().filter("");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

//    Вызывается при вводе поискового запроса
    @Override
    public boolean onQueryTextChange(String newText) {
        expandableListView.collapseGroup(lastExpandedPosition);
        adapter.getFilter().filter(newText);
        return false;
    }
}
