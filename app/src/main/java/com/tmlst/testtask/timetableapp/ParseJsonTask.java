package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleExpandableListAdapter;

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

/**
 * Created by User on 16.03.2018.
 */

public class ParseJsonTask extends AsyncTask<Void, Void, Model> implements SearchView.OnQueryTextListener {

    private static final String STATIONS_FROM = "citiesFrom";
    private static final String STATIONS_TO = "citiesTo";

    private Context context;
    private Model model;

    private ListView list;
    private ListAdapter adapter;
    private SearchView editsearch;

    ParseJsonTask(Context context) {
        this.context = context;
    }

    @Override
    protected Model doInBackground(Void... voids) {
        FileHelper fileHelper = new FileHelper(context);
        String jsonString = fileHelper.getJsonString();
        model = new Model();
        model.setCitiesFrom(getCities(jsonString, STATIONS_FROM));
        model.setCitiesTo(getCities(jsonString, STATIONS_TO));
        return model;
    }

    @Override
    protected void onPostExecute(Model model) {
        super.onPostExecute(model);

//        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
//        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
//
//        Map<String, String> map;
//        ArrayList<Map<String, String>> сhildDataItemList;
//
//        String groupFrom[] = new String[] { "cityName", "countryTitle" };
//        int groupTo[] = new int[] { android.R.id.text2, android.R.id.text1 };
//
//        String childFrom[] = new String[] { "stationName" };
//        int childTo[] = new int[] { android.R.id.text1 };
//
//        for (City city : model.getCitiesFrom()) {
//            map = new HashMap<>();
//            map.put("cityName", city.getCityTitle());
//            map.put("countryTitle", city.getCountryTitle());
//            groupDataList.add(map);
//
//            сhildDataItemList = new ArrayList<>();
//            for (Station station  : city.getStations()) {
//                map = new HashMap<>();
//                map.put("stationName", station.getStationTitle());
//                сhildDataItemList.add(map);
//            }
//            сhildDataList.add(сhildDataItemList);
//        }
//
//        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
//                context, groupDataList,
//                android.R.layout.simple_expandable_list_item_2, groupFrom,
//                groupTo, сhildDataList, android.R.layout.simple_list_item_1,
//                childFrom, childTo);
//
//        ExpandableListView expandableListView = ((Activity) context).
//                findViewById(R.id.stationsFrom);
//        expandableListView.setAdapter(adapter);

        list = ((Activity) context).findViewById(R.id.list_view);
        adapter = new ListAdapter(context, model.getCitiesFrom());
        list.setAdapter(adapter);

        editsearch = ((Activity) context).findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
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