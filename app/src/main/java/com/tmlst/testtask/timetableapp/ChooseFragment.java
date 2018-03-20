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

import static com.tmlst.testtask.timetableapp.MainActivity.CITYFROM;
import static com.tmlst.testtask.timetableapp.MainActivity.CITYTO;
import static com.tmlst.testtask.timetableapp.MainActivity.STATION_EQUAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseFragment extends Fragment implements SearchView.OnQueryTextListener, JsonParser.OnParseListener {

    private ListAdapter adapter;
    private ExpandableListView expandableListView;
    private String stationsType;
    SearchView editsearch;

    private Context mContext;

    private int lastExpandedPosition = -1;

    public ChooseFragment() {}

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

        expandableListView = view.findViewById(R.id.list_view);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Station station = State.getInstance().getModel().getCitiesFrom().
                            get(groupPosition).getStations().get(childPosition);
                    String stationInfo = "Страна:   " + station.getCountryTitle() + "\n" +
                            "Город:    " + station.getCityTitle() + "\n" +
                            "Область:  " + station.getRegionTitle() + "\n" +
                            "Район:    " + station.getDistrictTitle();

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

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                switch (stationsType) {
                    case CITYFROM:
                        long newStationFromId = adapter.getStationId(groupPosition, childPosition);
                        if (State.getInstance().getStationToId() != newStationFromId) {
                            State.getInstance().setStationFromId(newStationFromId);
                        } else {
                            Toast.makeText(mContext, STATION_EQUAL, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case CITYTO:
                        long newStationToId = adapter.getStationId(groupPosition, childPosition);
                        if (State.getInstance().getStationFromId() != newStationToId) {
                            State.getInstance().setStationToId(newStationToId);
                        } else {
                            Toast.makeText(mContext, STATION_EQUAL, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                //adapter.getFilter().filter("");// сброc фильтра
                // скрываем клавиатуру
                hideKeyBoard();

                ((Activity) mContext).onBackPressed();

                return false;
            }
        });

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

        JsonParser.setOnParseListener(this);
        setStationsAdapter();

        editsearch = view.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        editsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                expandableListView.collapseGroup(lastExpandedPosition);
            }
        });

        return view;
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager)
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editsearch.getWindowToken(), 0);
    }

    @Override
    public void onComplete() {
        setStationsAdapter();
    }

    private void setStationsAdapter() {
        switch (stationsType) {
            case CITYFROM:
                if (ListAdapter.adapterFrom != null) {
                    adapter = ListAdapter.adapterFrom;
                    adapter.notifyDataSetChanged();
                }
                break;
            case CITYTO:
                if (ListAdapter.adapterTo != null) {
                    adapter = ListAdapter.adapterTo;
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        expandableListView.setAdapter(adapter);
        adapter.getFilter().filter("");// сброc фильтра
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        expandableListView.collapseGroup(lastExpandedPosition);
        adapter.getFilter().filter(newText);
        return false;
    }
}
