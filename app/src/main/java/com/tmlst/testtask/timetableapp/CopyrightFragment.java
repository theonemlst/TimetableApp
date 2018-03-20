package com.tmlst.testtask.timetableapp;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CopyrightFragment extends Fragment {

    private Context mContext = null;

    public CopyrightFragment() {}

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_copyright, container, false);

        ((Activity) mContext).setTitle(R.string.about_title);

        TextView copyrightInfo = view.findViewById(R.id.copyright_info);
        copyrightInfo.setText(getCopyrightString());

        return view;
    }

    private String getCopyrightString() {
        String versionName = "";
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return getResources().getString(R.string.app_name) + " v " +
                versionName + "\n" + "©2018, Yaroslav Lagosha";
    }
}
