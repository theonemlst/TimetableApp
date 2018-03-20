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
 *   Класс адаптера ListView - меню экрана расписания
 */

public class MenuAdapter extends BaseAdapter {

    private ArrayList<MenuItemData> itemList;
    private LayoutInflater mInflater;
    private boolean isItemsEnabled = false;

    MenuAdapter(Context photosFragment, ArrayList<MenuItemData> results){
        itemList = results;
        mInflater = LayoutInflater.from(photosFragment);
    }

//    Будем запрещать переход на экран выбора станции до окончания парсинга
    @Override
    public boolean isEnabled(int position) {
        if (position < 2)
            return isItemsEnabled;
        else return true;
    }

    void setItemsEnabled() {
        isItemsEnabled = true;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return itemList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.menu_item, null);
            holder = new ViewHolder();
            holder.bigText = convertView.findViewById(R.id.big_text);
            holder.middleText = convertView.findViewById(R.id.middle_text);
            holder.smallText = convertView.findViewById(R.id.small_text);
            holder.icon = convertView.findViewById(R.id.item_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        Задаем внешний вид пунктов меню
        holder.bigText.setText(itemList.get(position).getBigText());
        holder.middleText.setText(itemList.get(position).getMiddleText());
        holder.smallText.setText(itemList.get(position).getSmallText());
        holder.icon.setImageResource(itemList.get(position).getImageSrc());

        return convertView;
    }

    static class ViewHolder{
        TextView bigText;
        TextView middleText;
        TextView smallText;
        ImageView icon;
    }
}