package com.tmlst.testtask.timetableapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private String fileName = null;
    private static final String JSON_FILE_NAME = "allStations.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, getJsonString().substring(3000000, 3000100),
                    Toast.LENGTH_SHORT).show();

    }

//    Читаем json из памяти и возвращаем в виде строки
    private String getJsonString() {

        fileName = getResources().getString(R.string.json_file_name);

        FileInputStream inputStream;
        byte buff[] = new byte[1];
        try {
            inputStream = openFileInput(fileName);
            buff = new byte[inputStream.available()];
            int len = inputStream.read(buff);
            inputStream.close();

//            Toast.makeText(this, String.valueOf(len),
//                    Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, new String(buff, 0, 20),
//                    Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException fnfe) {

            //Toast.makeText(this, "file's not exist, downloading it",
              //      Toast.LENGTH_SHORT).show();

            downloadJsonFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buff);
    }

//    Копируем файл json из каталога assets в internal storage
    private void downloadJsonFile() {
        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = getAssets().open(JSON_FILE_NAME);
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
