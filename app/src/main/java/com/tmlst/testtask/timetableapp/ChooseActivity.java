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


public class ChooseActivity extends Activity
        implements SearchView.OnQueryTextListener, JsonParser.OnParseListener {

    private ListAdapter adapter;
    private ExpandableListView expandableListView;

    private String stationsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseActivity.this);
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

                Station station = Model.getInstance().getCitiesFrom().
                        get(groupPosition).getStations().get(childPosition);

                Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                intent.putExtra("station", station);
                intent.putExtra("stationType",stationsType);
                setResult(RESULT_OK, intent);
                finish();

                return false;
            }
        });

//        TODO: improve logic
        JsonParser.setOnParseListener(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
             stationsType = intent.getStringExtra(MainActivity.STATION_TYPE);
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
                if (ListAdapter.adapterFrom != null) {
                    adapter = ListAdapter.adapterFrom;
                    adapter.notifyDataSetChanged();
                }
                break;
            case "TO":
                if (ListAdapter.adapterTo != null) {
                    adapter = ListAdapter.adapterTo;
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
        return false;
    }

}
