package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParseJsonTask().execute();

//        FileHelper fileHelper = new FileHelper(this);
//        String jsonAsString = fileHelper.getJsonString();
//        Toast.makeText(this, jsonAsString.substring(3000000, 3000100),
//                    Toast.LENGTH_SHORT).show();


    }

    private class ParseJsonTask extends AsyncTask<Void, Void, String> {

        static final String STATIONS_FROM = "citiesFrom";
        static final String STATIONS_TO = "citiesTo";

        @Override
        protected String doInBackground(Void... voids) {
            FileHelper fileHelper = new FileHelper(MainActivity.this);
            return fileHelper.getJsonString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            ListView stationsFromListView =
                    MainActivity.this.findViewById(R.id.stationsFrom);
            ListView stationsToListView =
                    MainActivity.this.findViewById(R.id.stationsTo);

            List<String> stationsFrom = getStationNames(jsonString, STATIONS_FROM);
            List<String> stationsTo = getStationNames(jsonString, STATIONS_TO);

            ArrayAdapter<String> arrayAdapterFrom = new ArrayAdapter<>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1, stationsFrom);

            ArrayAdapter<String> arrayAdapterTo = new ArrayAdapter<>(
                    MainActivity.this,
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

}
