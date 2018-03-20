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

/**
 *   Класс главной активности
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FROM = "FROM";
    public static final String TO = "TO";

    private static final String YEAR = "YEAR";
    private static final String MONTH = "MONTH";
    private static final String DAY = "DAY";

    private static final String STATION_FROM_ID = "stationFromId";
    private static final String STATION_TO_ID = "stationToId";

    public static final int NULL_STATION_INDEX = -1;
    private static final int NULL_DATE_MENBER = -1;

    private boolean doubleBackToExitPressedOnce = false;

    private AppState appState;

    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appState = AppState.getInstance();

//        Запускаем парсинг json
        Model model = appState.getModel();
        JsonParser jsonParser = new JsonParser(this, model);
        jsonParser.execute();
//        Инициализация бокового меню и панели действий
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Создаем и добавляем во фрагмент-менеджер фрагмент расписания
        TimetableFrag fragment = new TimetableFrag();
        fragment.setmContext(this);
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Сохраняем состояние приложения в SharedPreferences в MODE_PRIVATE режиме
        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();

        editor.putLong(STATION_FROM_ID, appState.getStationFromId());
        editor.putLong(STATION_TO_ID, appState.getStationToId());
        editor.putInt(YEAR, appState.getYear());
        editor.putInt(MONTH, appState.getMonth());
        editor.putInt(DAY, appState.getDay());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Восстанавливаем состояние из SharedPreferences
        SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);

        appState.setStationFromId(activityPreferences.getLong(STATION_FROM_ID, NULL_STATION_INDEX));
        appState.setStationToId(activityPreferences.getLong(STATION_TO_ID, NULL_STATION_INDEX));
        int year = activityPreferences.getInt(YEAR, NULL_DATE_MENBER);
        int month = activityPreferences.getInt(MONTH, NULL_DATE_MENBER);
        int day = activityPreferences.getInt(DAY, NULL_DATE_MENBER);
        if (year != NULL_DATE_MENBER && month != NULL_DATE_MENBER && day != NULL_DATE_MENBER)
            appState.getCalendar().set(year, month, day);
    }

    @Override
    public void onBackPressed() {
//        Обработка BACK BUTTON
//        Если "шторка" меню открыта - закрываем ее
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
//            Иначе если в стеке один фрагмент - и BACK BUTTON нажата повторно -
//                    выходим из приложения путем двукратного программного нажатия
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                super.onBackPressed();
                return;
            }
//            При первом нажатии - выводим подсказку в Toast
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.twice_click_for_exit_toast_text),
                    Toast.LENGTH_SHORT).show();
//            Ждем повторного нажатия 2 секунды
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
//            Достаем предыдущий фрагмент
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
//        Обрабатываем нажатия на пункты бокового меню
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.nav_timetable) {
            TimetableFrag fragment = new TimetableFrag();
            fragment.setmContext(this);
            fragmentTransaction.replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_about) {
            CopyrightFrag fragment = new CopyrightFrag();
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