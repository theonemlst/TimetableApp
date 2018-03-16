package com.tmlst.testtask.timetableapp;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by User on 16.03.2018.
 */

class FileHelper {

    private Context context;
    private String fileName = null;
    private static final String JSON_FILE_NAME = "allStations.json";

    FileHelper(Context context) {

        this.context = context;
        fileName = context.getResources().getString(R.string.json_file_name);

//        File dir = context.getFilesDir();
//        File file = new File(dir, fileName);
//        boolean deleted = file.delete();
    }

    //    Читаем json из памяти и возвращаем в виде строки
    String getJsonString() {

        try {
            context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            copyJsonFile();
//            Toast.makeText(context, "copy json from assets",
//                    Toast.LENGTH_SHORT).show();
        }

        FileInputStream inputStream;
        byte buff[] = new byte[1];
        try {
            inputStream = context.openFileInput(fileName);
            buff = new byte[inputStream.available()];
            int len = inputStream.read(buff);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buff);
    }

    //    Копируем файл json из каталога assets в internal storage
    private void copyJsonFile() {
        InputStream inputStream;
        OutputStream outputStream;
        try {
            inputStream = context.getAssets().open(JSON_FILE_NAME);
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);

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
