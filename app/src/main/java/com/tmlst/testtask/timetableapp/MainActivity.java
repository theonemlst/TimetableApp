package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;


import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.tmlst.testtask.timetableapp.model.Model;

import java.util.Calendar;

import static com.tmlst.testtask.timetableapp.TimetableFragment.DAY;
import static com.tmlst.testtask.timetableapp.TimetableFragment.MONTH;
import static com.tmlst.testtask.timetableapp.TimetableFragment.YEAR;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CITYFROM = "FROM";
    public static final String CITYTO = "TO";

    public static final String STATION_TYPE = "stationType";
    public static final String STATION = "station";

    public static final String STATION_EQUAL = "stations are equal!";

    public static final String STATION_FROM_ID = "stationFromId";
    public static final String STATION_TO_ID = "stationToId";

    boolean doubleBackToExitPressedOnce = false;

    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Model model = State.getInstance().getModel();
        JsonParser jsonParser = new JsonParser(this, model);
        jsonParser.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TimetableFragment fragment = new TimetableFragment();
        fragment.setmContext(this);
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();

        editor.putLong(STATION_FROM_ID, State.getInstance().getStationFromId());
        editor.putLong(STATION_TO_ID, State.getInstance().getStationToId());
        editor.putInt(YEAR, State.getInstance().getYear());
        editor.putInt(MONTH, State.getInstance().getMonth());
        editor.putInt(DAY, State.getInstance().getDay());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);

        State.getInstance().setStationFromId(activityPreferences.getLong(STATION_FROM_ID, -1));
        State.getInstance().setStationToId(activityPreferences.getLong(STATION_TO_ID, -1));
        int year = activityPreferences.getInt(YEAR, -1);
        int month = activityPreferences.getInt(MONTH, -1);
        int day = activityPreferences.getInt(DAY, -1);
        if (year != -1 && month != -1 && day != -1)
            State.getInstance().getCalendar().set(year, month, day);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.nav_timetable) {
            TimetableFragment fragment = new TimetableFragment();
            fragment.setmContext(this);
            fragmentTransaction.replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_about) {
            CopyrightFragment fragment = new CopyrightFragment();
            fragment.setmContext(this);
            fragmentTransaction.replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}