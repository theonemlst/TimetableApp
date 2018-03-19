package com.tmlst.testtask.timetableapp.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by User on 16.03.2018.
 */
                     // TODO: no more need to implement this
public class Station implements Parcelable{

    private String countryTitle;
    private Point point;
    private String districtTitle;
    private int cityId;
    private String cityTitle;
    private String regionTitle;
    private int stationId;
    private String stationTitle;

    public Station() {
    }

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getDistrictTitle() {
        return districtTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        this.districtTitle = districtTitle;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getRegionTitle() {
        return regionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        this.regionTitle = regionTitle;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return stationId == station.stationId;
    }

    @Override
    public int hashCode() {
        return stationId;
    }

// Parcelable part

    private Station(Parcel in){
        String[] data = new String[9];

        in.readStringArray(data);

        this.countryTitle = data[0];
        Point point = new Point();
        point.setLongitude(Double.valueOf(data[1]));
        point.setLatitude(Double.valueOf(data[2]));
        this.point = point;
        this.districtTitle = data[3];
        this.cityId = Integer.valueOf(data[4]);
        this.cityTitle = data[5];
        this.regionTitle = data[6];
        this.stationId = Integer.valueOf(data[7]);
        this.stationTitle = data[8];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.countryTitle,
                String.valueOf(this.point.getLongitude()),
                String.valueOf(this.point.getLatitude()),
                this.districtTitle,
                String.valueOf(this.cityId),
                this.cityTitle,
                this.regionTitle,
                String.valueOf(this.stationId),
                this.stationTitle
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
