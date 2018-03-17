package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tmlst.testtask.timetableapp.model.City;
import com.tmlst.testtask.timetableapp.model.Point;
import com.tmlst.testtask.timetableapp.model.Station;

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

        List<City> stationsFrom = getCities(jsonString, STATIONS_FROM);
        List<City> stationsTo = getCities(jsonString, STATIONS_TO);

        ArrayAdapter<City> arrayAdapterFrom = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsFrom);

        ArrayAdapter<City> arrayAdapterTo = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1, stationsTo);

        stationsFromListView.setAdapter(arrayAdapterFrom);
        stationsToListView.setAdapter(arrayAdapterTo);
    }

    private List<City> getCities(String jsonString, String citiesType) {

        List<City> cities = new ArrayList<>();

        try {
            JSONObject jsonDataObj = new JSONObject(jsonString);
            JSONArray citiesJSONArray = jsonDataObj.getJSONArray(citiesType);

            for (int j = 0; j <citiesJSONArray.length(); j++) {

                JSONObject cityJSONObject = citiesJSONArray.getJSONObject(j);
                City city = new City();

                JSONObject pointJSONObject = cityJSONObject.getJSONObject("point");
                Point point = new Point();

                point.setLongitude(pointJSONObject.getDouble("longitude"));
                point.setLatitude(pointJSONObject.getDouble("latitude"));

                city.setPoint(point);
                city.setCountryTitle(cityJSONObject.getString("countryTitle"));
                city.setDistrictTitle(cityJSONObject.getString("districtTitle"));
                city.setCityId(cityJSONObject.getInt("cityId"));
                city.setCityTitle(cityJSONObject.getString("cityTitle"));
                city.setRegionTitle(cityJSONObject.getString("regionTitle"));

                city.setStations(getCityStations(cityJSONObject));

                cities.add(city);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return cities;
    }

    private List<Station> getCityStations(JSONObject cityJSONObject) {

        List<Station> cityStations = new ArrayList<>();
        try{
            JSONArray cityStationsJSONArray = cityJSONObject.getJSONArray("stations");

            for (int i = 0; i < cityStationsJSONArray.length(); i++) {

                JSONObject stationJSONObject = cityStationsJSONArray.getJSONObject(i);
                Station station = new Station();

                JSONObject pointJSONObject = stationJSONObject.getJSONObject("point");
                Point point = new Point();

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

                cityStations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityStations;
    }
}