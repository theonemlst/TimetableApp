package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;

public class ChooseStationActivity extends Activity
        implements SearchView.OnQueryTextListener, ParseJsonTask.OnParseListener {

    private SearchableExpandableListAdapter adapter;
    private ExpandableListView expandableListView;

    private String stationsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_station);

        expandableListView = findViewById(R.id.list_view);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Station station = Model.getInstance().getCitiesFrom().
                            get(groupPosition).getStations().get(childPosition);
                    String stationInfo = "Страна:   " + station.getCountryTitle() + "\n" +
                            "Город:    " + station.getCityTitle() + "\n" +
                            "Область:  " + station.getRegionTitle() + "\n" +
                            "Район:    " + station.getDistrictTitle();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseStationActivity.this);
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

        ParseJsonTask.setOnParseListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
             stationsType = intent.getStringExtra("type");
        }
        setStationsAdapter();

        SearchView editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public void onComplete() {
        setStationsAdapter();
    }

    private void setStationsAdapter() {
        switch (stationsType) {
            case "FROM":
                if (SearchableExpandableListAdapter.adapterFrom != null) {
                    adapter = SearchableExpandableListAdapter.adapterFrom;
                    adapter.notifyDataSetChanged();
                }
                break;
            case "TO":
                if (SearchableExpandableListAdapter.adapterTo != null) {
                    adapter = SearchableExpandableListAdapter.adapterTo;
                    adapter.notifyDataSetChanged();
                }
                break;
        }
        expandableListView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        //expandableListView.smoothScrollToPosition(0);
        return false;
    }

}
