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

        List<City> stationsFrom = getStationNames(jsonString, STATIONS_FROM);
        List<City> stationsTo = getStationNames(jsonString, STATIONS_TO);

        ArrayAdapter<City> arrayAdapterFrom = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsFrom);

        ArrayAdapter<City> arrayAdapterTo = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsTo);

        stationsFromListView.setAdapter(arrayAdapterFrom);
        stationsToListView.setAdapter(arrayAdapterTo);
    }

    private List<City> getStationNames(String jsonString, String stationType) {

        List<City> cities = new ArrayList<>();
        JSONObject dataJsonObj;

        try {

            dataJsonObj = new JSONObject(jsonString);
            JSONArray citiesJSONArray = dataJsonObj.getJSONArray(stationType);
            Point point;


            for (int j = 0; j <citiesJSONArray.length(); j++) {

                JSONObject cityJSONObject = citiesJSONArray.getJSONObject(j);
                City city = new City();

                JSONObject pointJSONObject = cityJSONObject.getJSONObject("point");
                point = new Point();

                point.setLongitude(pointJSONObject.getDouble("longitude"));
                point.setLatitude(pointJSONObject.getDouble("latitude"));

                city.setPoint(point);
                city.setCountryTitle(cityJSONObject.getString("countryTitle"));
                city.setDistrictTitle(cityJSONObject.getString("districtTitle"));
                city.setCityId(cityJSONObject.getInt("cityId"));
                city.setCityTitle(cityJSONObject.getString("cityTitle"));
                city.setRegionTitle(cityJSONObject.getString("regionTitle"));

                JSONArray cityStationsJSONArray = cityJSONObject.getJSONArray("stations");
                List<Station> stations = new ArrayList<>();

                for (int i = 0; i < cityStationsJSONArray.length(); i++) {

                    JSONObject stationJSONObject = cityStationsJSONArray.getJSONObject(i);
                    Station station = new Station();

                    pointJSONObject = stationJSONObject.getJSONObject("point");
                    point = new Point();

                    point.setLongitude(pointJSONObject.getDouble("longitude"));
                    point.setLatitude(pointJSONObject.getDouble("latitude"));

                    station.setPoint(point);
                    station.setCountryTitle(stationJSONObject.getString("countryTitle"));
                    station.setDistrictTitle(stationJSONObject.getString("districtTitle"));
                    station.setCityId(stationJSONObject.getInt("cityId"));
                    station.setCityTitle(stationJSONObject.getString("cityTitle"));
                    station.setRegionTitle(stationJSONObject.getString("regionTitle"));
                    station.setStationId(stationJSONObject.getInt("stationId"));
                    station.setStationTitle(stationJSONObject.getString("stationTitle"));

                    stations.add(station);
                }

                city.setStations(stations);
                cities.add(city);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        return cities;
    }
}