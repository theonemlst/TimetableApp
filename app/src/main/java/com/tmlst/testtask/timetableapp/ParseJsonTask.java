package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.03.2018.
 */

public class ParseJsonTask extends AsyncTask<Void, Void, String> {

    private static final String STATIONS_FROM = "citiesFrom";
    private static final String STATIONS_TO = "citiesTo";

    private Context context;

    ParseJsonTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        FileHelper fileHelper = new FileHelper(context);
        return fileHelper.getJsonString();
    }

    @Override
    protected void onPostExecute(String jsonString) {
        super.onPostExecute(jsonString);

        ListView stationsFromListView =
                ((Activity) context).findViewById(R.id.stationsFrom);
        ListView stationsToListView =
                ((Activity) context).findViewById(R.id.stationsTo);

        List<String> stationsFrom = getStationNames(jsonString, STATIONS_FROM);
        List<String> stationsTo = getStationNames(jsonString, STATIONS_TO);

        ArrayAdapter<String> arrayAdapterFrom = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsFrom);

        ArrayAdapter<String> arrayAdapterTo = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsTo);

        stationsFromListView.setAdapter(arrayAdapterFrom);
        stationsToListView.setAdapter(arrayAdapterTo);
    }

    private List<String> getStationNames(String jsonString, String stationType) {

        List<String> stationsList = new ArrayList<>();
        JSONObject dataJsonObj;

        try {

            dataJsonObj = new JSONObject(jsonString);
            JSONArray citiesFrom = dataJsonObj.getJSONArray(stationType);

            for (int j = 0; j <citiesFrom.length(); j++) {

                JSONObject cityFrom = citiesFrom.getJSONObject(j);

                JSONArray cityFromStations = cityFrom.
                        getJSONArray("stations");

                for (int i = 0; i < cityFromStations.length(); i++) {
                    JSONObject station = cityFromStations.getJSONObject(i);

//                        JSONObject point = station.getJSONObject("point");
//                        String longitude = point.getString("longitude");
//                        String latitude = point.getString("latitude");
//
//                        String countryTitle = station.getString("countryTitle");
//                        String districtTitle = station.getString("districtTitle");
//                        String cityId = station.getString("cityId");
//                        String cityTitle = station.getString("cityTitle");
//                        String regionTitle = station.getString("regionTitle");
//                        String stationId = station.getString("stationId");
                    String stationTitle = station.getString("stationTitle");

                    stationsList.add(stationTitle);
                }
            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        return stationsList;
    }
}