package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Station;


public class MainActivity extends Activity implements
        SearchView.OnQueryTextListener, ParseJsonTask.OnParseListener {

    private SearchableExpandableListAdapter adapter;
    private SearchView editsearch;
    private Model model;

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new Model();
        ParseJsonTask parseJsonTask = new ParseJsonTask(this, model);
        parseJsonTask.setOnParseListener(this);
        parseJsonTask.execute();

        expandableListView = findViewById(R.id.list_view);
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    Station station = model.getCitiesFrom().
                            get(groupPosition).getStations().get(childPosition);
                    String stationInfo = "Страна:   " + station.getCountryTitle() + "\n" +
                                "Город:    " + station.getCityTitle() + "\n" +
                                "Область:  " + station.getRegionTitle() + "\n" +
                                "Район:    " + station.getDistrictTitle();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public void onComplete(SearchableExpandableListAdapter adapter) {
        this.adapter = adapter;
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
