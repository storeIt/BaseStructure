package com.example.om.basestructure.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.om.basestructure.db.service.ToDoDbService;
import com.example.om.basestructure.model.ServerToDo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by android on 25.03.18.
 */

public class DownloadToDoService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = downloadToDos();
                String result = parseInputStream(inputStream);
                List<ServerToDo> todoList = null;
                if (!TextUtils.isEmpty(result)) {
                    todoList = parseResponseToJSON(result);
                }
                ToDoDbService.getInstance().saveToDos(todoList);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private InputStream downloadToDos() {
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/todos/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(35000);
            return httpURLConnection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseInputStream(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 10);
            StringBuilder stringBuilder = new StringBuilder();
            String stringLine = null;
            while ((stringLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(stringLine + "\n");
            }
            result = stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private List<ServerToDo> parseResponseToJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, new TypeReference<List<ServerToDo>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
