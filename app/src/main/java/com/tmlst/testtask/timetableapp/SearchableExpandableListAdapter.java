package com.tmlst.testtask.timetableapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by User on 17.03.2018.
 */

public class SearchableExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {

    private ArrayList<Map<String, String>> originalCityList = null;
    private ArrayList<Map<String, String>> filteredCityList = null;
    private ArrayList<ArrayList<Map<String, String>>> originalStationList = null;
    private ArrayList<ArrayList<Map<String, String>>> filteredStationList = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    SearchableExpandableListAdapter(Context context,
                                           ArrayList<Map<String, String>> cityList,
                                           ArrayList<ArrayList<Map<String, String>>> stationList) {
        this.filteredCityList = cityList;
        this.originalCityList = cityList;
        this.originalStationList = stationList;
        this.filteredStationList = stationList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return filteredCityList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filteredStationList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filteredCityList.get(groupPosition).get("cityName");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filteredStationList.get(groupPosition).get(childPosition).get("stationName");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v;
        String cityName = (String) getGroup(groupPosition);
        if (convertView == null) {
            v = mInflater.inflate(R.layout.group_items, null);
        } else {
            v = convertView;
        }
        TextView childItem = v.findViewById(R.id.city_name);
        childItem.setText(cityName);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v;
        String stationName = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            v = mInflater.inflate(R.layout.child_items, null);
        } else {
            v = convertView;
        }
        TextView childItem = v.findViewById(R.id.child_item);
        childItem.setText(stationName);
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            int cityCount = originalCityList.size();
            int stationCount = originalStationList.size();
            final ArrayList<Map<String, String>> newCityList =
                    new ArrayList<>(cityCount);
            final ArrayList<ArrayList<Map<String, String>>> newStationList =
                    new ArrayList<>(stationCount);

            for (int i = 0; i < cityCount; i++) {
                ArrayList<Map<String, String>> matchedStationNames = new ArrayList<>();
                ArrayList<Map<String, String>> stationsOfCity =
                        originalStationList.get(i);
                for (Map<String, String> station : stationsOfCity) {
                    if (station.get("stationName").
                            toLowerCase().contains(filterString))
                        matchedStationNames.add(station);
                }
                if (matchedStationNames.size() > 0) {
                    newCityList.add(originalCityList.get(i));
                    newStationList.add(matchedStationNames);
                }
            }
            filteredCityList = newCityList;
            filteredStationList = newStationList;

            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
