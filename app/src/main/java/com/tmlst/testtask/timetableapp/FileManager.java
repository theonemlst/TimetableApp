package com.tmlst.testtask.timetableapp;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *    Класс для работы с json-файлом
 */

class FileManager {

    private Context context;
    private String fileName = null;
    private static final String JSON_FILE_NAME = "allStations.json";

    FileManager(Context context) {
        this.context = context;
        fileName = context.getResources().getString(R.string.json_file_name);
//TODO - удалить удаление
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);
        boolean deleted = file.delete();
    }

    //    Читаем json из памяти и возвращаем в виде строки
    String getJsonString() {

        try {
            context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            copyJsonFile();
        }

        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = context.openFileInput(fileName);
            outputStream = new ByteArrayOutputStream();

            byte buff[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputStream == null ? "" : outputStream.toString();
    }

    //    Копируем файл json из каталога assets в internal storage
    private void copyJsonFile() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
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
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
