package com.tmlst.testtask.timetableapp;

/**
 * Created by User on 20.03.2018.
 */

class ListViewData {
    private String countryTitle;
    private String cityTitle;
    private String stationTitle;
    private int src;

    public ListViewData(String countryTitle, String cityTitle, String stationTitle, int src) {
        this.countryTitle = countryTitle;
        this.cityTitle = cityTitle;
        this.stationTitle = stationTitle;
        this.src = src;
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

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }
}
