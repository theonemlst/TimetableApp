package com.tmlst.testtask.timetableapp;

import com.tmlst.testtask.timetableapp.model.Model;

import java.util.Calendar;

/**
 *    Синглтон - хранит текущие выбранные станции, дату и данные из json
 */

class AppState {

    private static final AppState ourInstance = new AppState();

    private long stationFromId = -1;
    private long stationToId = -1;
    private Calendar calendar = Calendar.getInstance();
    private Model model = new Model();

    static AppState getInstance() {
        return ourInstance;
    }

    private AppState() {
    }

    int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    Model getModel() {
        return model;
    }

    long getStationFromId() {
        return stationFromId;
    }

    void setStationFromId(long stationFromId) {
        this.stationFromId = stationFromId;
    }

    long getStationToId() {
        return stationToId;
    }

    void setStationToId(long stationToId) {
        this.stationToId = stationToId;
    }

    Calendar getCalendar() {
        return calendar;
    }

    void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
