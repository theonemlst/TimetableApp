package com.tmlst.testtask.timetableapp;

import android.content.Context;
import android.os.AsyncTask;

import com.tmlst.testtask.timetableapp.model.City;
import com.tmlst.testtask.timetableapp.model.Model;
import com.tmlst.testtask.timetableapp.model.Point;
import com.tmlst.testtask.timetableapp.model.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tmlst.testtask.timetableapp.MainActivity.CITYFROM;
import static com.tmlst.testtask.timetableapp.MainActivity.CITYTO;

/**
 * Created by User on 16.03.2018.
 */

public class JsonParser extends AsyncTask<Void, Void, Model> {

    private static final String STATIONS_FROM = "citiesFrom";
    private static final String STATIONS_TO = "citiesTo";

    private static List<OnParseListener> mListeners = new ArrayList<>();

    public interface OnParseListener
    {
        void onComplete();
    }

    private Context context;
    private Model mModel;

    JsonParser(Context context, Model mModel) {
        this.context = context;
        this.mModel = mModel;
    }

    @Override
    protected Model doInBackground(Void... voids) {
        FileHelper fileHelper = new FileHelper(context);
        String jsonString = fileHelper.getJsonString();
        mModel.setCitiesFrom(getCities(jsonString, STATIONS_FROM));
        mModel.setCitiesTo(getCities(jsonString, STATIONS_TO));
        return mModel;
    }

    @Override
    protected void onPostExecute(Model model) {
        ListAdapter.adapterFrom = getAdapter(mModel, CITYFROM);
        ListAdapter.adapterTo = getAdapter(mModel, CITYTO);
        for (OnParseListener listener: mListeners) {
            listener.onComplete();
        }
    }

    static void setOnParseListener(OnParseListener listener)
    {
        mListeners.add(listener);
    }

    private ListAdapter getAdapter(Model model, String citiesType) {

        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        Map<String, String> map;
        ArrayList<Map<String, String>> сhildDataItemList;

        List<City> cities = null;
        if (citiesType.equals(CITYFROM))
            cities = model.getCitiesFrom();
        if (citiesType.equals(CITYTO))
            cities = model.getCitiesTo();

        for (City city : cities) {
            map = new HashMap<>();
            map.put("cityName", city.getCityTitle());
            map.put("countryTitle", city.getCountryTitle());
            groupDataList.add(map);

            сhildDataItemList = new ArrayList<>();
            for (Station station  : city.getStations()) {
                map = new HashMap<>();
                map.put("stationName", station.getStationTitle());
                map.put("stationId", String.valueOf(station.getStationId()));
                сhildDataItemList.add(map);
            }
            сhildDataList.add(сhildDataItemList);
        }

        return new ListAdapter(context, groupDataList, сhildDataList);
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