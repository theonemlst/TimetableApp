package com.tmlst.testtask.timetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tmlst.testtask.timetableapp.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by User on 17.03.2018.
 */

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<City> cities = null;
    private ArrayList<City> arraylist;

    ListAdapter(Context context, List<City> cities) {
        mContext = context;
        this.cities = cities;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(cities);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            holder.name = view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(cities.get(position).getCityTitle());
        return view;
    }

    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        cities.clear();
        if (charText.length() == 0) {
            cities.addAll(arraylist);
        } else {
            for (City wp : arraylist) {
                if (wp.getCityTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    cities.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}