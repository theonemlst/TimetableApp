package com.tmlst.testtask.timetableapp.model;

import java.util.List;

/**
 * Created by User on 17.03.2018.
 */

public class Model {

    private List<City> citiesFrom;
    private List<City> citiesTo;

    public List<City> getCitiesFrom() {
        return citiesFrom;
    }

    public void setCitiesFrom(List<City> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }

    public List<City> getCitiesTo() {
        return citiesTo;
    }

    public void setCitiesTo(List<City> citiesTo) {
        this.citiesTo = citiesTo;
    }
}
