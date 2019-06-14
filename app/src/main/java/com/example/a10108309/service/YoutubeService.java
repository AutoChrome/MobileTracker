package com.example.a10108309.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;

import com.example.a10108309.phoneapplication.YoutubeFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class YoutubeService extends AsyncTask<String, Void, JSONArray> {

    private String key = "AIzaSyD18yd26kZODHMsFVKdcjk0ekNwFEbRcvc";
    private YoutubeFragment listener;
    private String query;
    private JSONArray results;

    public YoutubeService(String query, YoutubeFragment fragment){
        this.query = query;
        this.listener = fragment;
    }

    public JSONArray getResults(){
        return this.results;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        URL url;
        boolean error = false;
        HttpURLConnection httpURLConnection = null;
        StringBuilder result = new StringBuilder();

        try{
            url = new URL("https://www.googleapis.com/youtube/v3/search?key="+ key +"&part=snippet&q=" + query + "&maxResults=3");

            httpURLConnection = (HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                result.append(line);
            }

            JSONObject jsonObject = new JSONObject(result.toString());
            results = jsonObject.getJSONArray("items");
            listener.serviceComplete(results);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return results;
    }
}
