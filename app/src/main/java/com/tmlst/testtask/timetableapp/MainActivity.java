package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

        @Override
        protected String doInBackground(Void... voids) {
            FileHelper fileHelper = new FileHelper(MainActivity.this);
            return fileHelper.getJsonString();
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            JSONObject dataJsonObj;
            String firstCityName;

            try {

                dataJsonObj = new JSONObject(jsonString);
                JSONArray citiesFrom = dataJsonObj.getJSONArray("citiesFrom");

                // выведем название первого города отправления
                JSONObject firstCityFrom = citiesFrom.getJSONObject(0);
                firstCityName = firstCityFrom.getString("cityTitle");

                Toast.makeText(MainActivity.this, firstCityName,
                        Toast.LENGTH_SHORT).show();

                // выведем название 11-го города отправления
                firstCityFrom = citiesFrom.getJSONObject(10);
                firstCityName = firstCityFrom.getString("cityTitle");

                Toast.makeText(MainActivity.this, firstCityName,
                        Toast.LENGTH_SHORT).show();

                // перечислим его станции
                JSONArray eleventhCityStations = firstCityFrom.
                        getJSONArray("stations");

                for (int i = 0; i < eleventhCityStations.length(); i++) {
                    JSONObject station = eleventhCityStations.getJSONObject(i);

                    JSONObject point = station.getJSONObject("point");
                    String longitude = point.getString("longitude");
                    String latitude = point.getString("latitude");

                    String countryTitle = station.getString("countryTitle");
                    String districtTitle = station.getString("districtTitle");
                    String cityId = station.getString("cityId");
                    String cityTitle = station.getString("cityTitle");
                    String regionTitle = station.getString("regionTitle");
                    String stationId = station.getString("stationId");
                    String stationTitle = station.getString("stationTitle");

                    String stationInfo = countryTitle + "\n" + longitude + "\n" +
                            latitude + "\n" + districtTitle + "\n" + cityId + "\n" +
                            cityTitle + "\n" + regionTitle + "\n" + stationId + "\n" +
                            stationTitle;

                    Toast.makeText(MainActivity.this, stationInfo,
                            Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
