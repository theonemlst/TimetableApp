package com.tmlst.testtask.timetableapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CopyrightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);

        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        TextView copyrightInfo =  findViewById(R.id.copyright_info);
        String copyrightString =
                getResources().getString(R.string.app_name) + " v" +
                        versionName + "\n" + "©2018, Yaroslav Lagosha";
        copyrightInfo.setText(copyrightString);
    }
}