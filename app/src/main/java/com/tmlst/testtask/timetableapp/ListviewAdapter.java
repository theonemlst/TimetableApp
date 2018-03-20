package com.tmlst.testtask.timetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 20.03.2018.
 */

public class ListviewAdapter extends BaseAdapter {

    private static ArrayList<ListViewData> listContact;

    private LayoutInflater mInflater;
    private boolean isItemsEnabled = false;

    ListviewAdapter(Context photosFragment, ArrayList<ListViewData> results){
        listContact = results;
        mInflater = LayoutInflater.from(photosFragment);
    }

    public static void setListContact(ArrayList<ListViewData> listContact) {
        ListviewAdapter.listContact = listContact;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position < 2)
            return isItemsEnabled;
        else return true;
    }

    public void setItemsEnabled(boolean enabled) {
        isItemsEnabled = enabled;
    }

    @Override
    public int getCount() {
        return listContact.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listContact.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.countryName = convertView.findViewById(R.id.country_title);
            holder.cityName = convertView.findViewById(R.id.city_title);
            holder.stationName = convertView.findViewById(R.id.station_title);
            holder.icon = convertView.findViewById(R.id.timetbl_item_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.countryName.setText(listContact.get(position).getCountryTitle());
        holder.cityName.setText(listContact.get(position).getCityTitle());
        holder.stationName.setText(listContact.get(position).getStationTitle());
        holder.icon.setImageResource(listContact.get(position).getSrc());

        return convertView;
    }

    static class ViewHolder{
        TextView countryName, cityName, stationName;
        ImageView icon;
    }
}