package com.tmlst.testtask.timetableapp;

/**
 * Created by User on 20.03.2018.
 */

class ListViewData {
    private String countryTitle;
    private String cityTitle;
    private String stationTitle;

    ListViewData(String countryTitle, String cityTitle, String stationTitle) {
        this.countryTitle = countryTitle;
        this.cityTitle = cityTitle;
        this.stationTitle = stationTitle;
    }

    String getCountryTitle() {
        return countryTitle;
    }

    void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    String getCityTitle() {
        return cityTitle;
    }

    void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    String getStationTitle() {
        return stationTitle;
    }

    void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }
}
