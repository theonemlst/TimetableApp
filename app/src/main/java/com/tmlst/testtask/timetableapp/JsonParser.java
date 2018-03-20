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

import static com.tmlst.testtask.timetableapp.ListAdapter.CITY_TITLE;
import static com.tmlst.testtask.timetableapp.ListAdapter.COUNTRY_TITLE;
import static com.tmlst.testtask.timetableapp.ListAdapter.STATION_ID;
import static com.tmlst.testtask.timetableapp.ListAdapter.STATION_TITLE;
import static com.tmlst.testtask.timetableapp.MainActivity.FROM;
import static com.tmlst.testtask.timetableapp.MainActivity.TO;

/**
 *   Парсер json-файла
 */

public class JsonParser extends AsyncTask<Void, Void, Model> {

    private static final String STATIONS_FROM = "citiesFrom";
    private static final String STATIONS_TO = "citiesTo";

    private static final String STATIONS_ARRAY = "stations";
    private static final String POINT = "point";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String DISTRICT_TITLE = "districtTitle";
    private static final String CITY_ID = "cityId";
    private static final String REGION_TITLE = "regionTitle";

//    Слушатели, ожидающие окончания парсинга
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

//    Парсим json и добавляем города в модель в отдельном потоке
    @Override
    protected Model doInBackground(Void... voids) {
        FileManager fileManager = new FileManager(context);
        String jsonString = fileManager.getJsonString();
        mModel.setCitiesFrom(getCities(jsonString, STATIONS_FROM));
        mModel.setCitiesTo(getCities(jsonString, STATIONS_TO));
        return mModel;
    }

    @Override
    protected void onPostExecute(Model model) {
//        Инициализируем адаптеры для двух типов станций и оповещаем слушателей
        ListAdapter.setAdapterFrom(getAdapter(mModel, FROM));
        ListAdapter.setAdapterTo(getAdapter(mModel, TO));
        for (OnParseListener listener: mListeners) {
            listener.onComplete();
        }
    }

    static void setOnParseListener(OnParseListener listener)
    {
        mListeners.add(listener);
    }

//    Возвращает адаптер для станций
    private ListAdapter getAdapter(Model model, String citiesType) {

        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();

        Map<String, String> map;
        ArrayList<Map<String, String>> сhildDataItemList;

        List<City> cities = new ArrayList<>();
        if (FROM.equals(citiesType))
            cities = model.getCitiesFrom();
        if (TO.equals(citiesType))
            cities = model.getCitiesTo();

        for (City city : cities) {
            map = new HashMap<>();
            map.put(CITY_TITLE, city.getCityTitle());
            map.put(COUNTRY_TITLE, city.getCountryTitle());
            groupDataList.add(map);

            сhildDataItemList = new ArrayList<>();
            for (Station station  : city.getStations()) {
                map = new HashMap<>();
                map.put(STATION_TITLE, station.getStationTitle());
                map.put(STATION_ID, String.valueOf(station.getStationId()));
                сhildDataItemList.add(map);
            }
            сhildDataList.add(сhildDataItemList);
        }

        return new ListAdapter(context, groupDataList, сhildDataList);
    }

//    Возвращает список городов заданного типа из строки json
    private List<City> getCities(String jsonString, String citiesType) {

        List<City> cities = new ArrayList<>();

        try {
            JSONObject jsonDataObj = new JSONObject(jsonString);
            JSONArray citiesJSONArray = jsonDataObj.getJSONArray(citiesType);

            for (int j = 0; j <citiesJSONArray.length(); j++) {

                JSONObject cityJSONObject = citiesJSONArray.getJSONObject(j);
                City city = new City();

                JSONObject pointJSONObject = cityJSONObject.getJSONObject(POINT);
                Point point = new Point();

                point.setLongitude(pointJSONObject.getDouble(LONGITUDE));
                point.setLatitude(pointJSONObject.getDouble(LATITUDE));

                city.setPoint(point);
                city.setCountryTitle(cityJSONObject.getString(COUNTRY_TITLE));
                city.setDistrictTitle(cityJSONObject.getString(DISTRICT_TITLE));
                city.setCityId(cityJSONObject.getInt(CITY_ID));
                city.setCityTitle(cityJSONObject.getString(CITY_TITLE));
                city.setRegionTitle(cityJSONObject.getString(REGION_TITLE));

                city.setStations(getCityStations(cityJSONObject));

                cities.add(city);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        return cities;
    }
//    Возвращает список станций города
    private List<Station> getCityStations(JSONObject cityJSONObject) {

        List<Station> cityStations = new ArrayList<>();
        try{
            JSONArray cityStationsJSONArray = cityJSONObject.getJSONArray(STATIONS_ARRAY);

            for (int i = 0; i < cityStationsJSONArray.length(); i++) {

                JSONObject stationJSONObject = cityStationsJSONArray.getJSONObject(i);
                Station station = new Station();

                JSONObject pointJSONObject = stationJSONObject.getJSONObject(POINT);
                Point point = new Point();

                point.setLongitude(pointJSONObject.getDouble(LONGITUDE));
                point.setLatitude(pointJSONObject.getDouble(LATITUDE));

                station.setPoint(point);
                station.setCountryTitle(stationJSONObject.getString(COUNTRY_TITLE));
                station.setDistrictTitle(stationJSONObject.getString(DISTRICT_TITLE));
                station.setCityId(stationJSONObject.getInt(CITY_ID));
                station.setCityTitle(stationJSONObject.getString(CITY_TITLE));
                station.setRegionTitle(stationJSONObject.getString(REGION_TITLE));
                station.setStationId(stationJSONObject.getInt(STATION_ID));
                station.setStationTitle(stationJSONObject.getString(STATION_TITLE));

                cityStations.add(station);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityStations;
    }
}