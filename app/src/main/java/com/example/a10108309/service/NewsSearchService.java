package com.example.a10108309.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NewsSearchService extends AbstractService {

    private String query;
    private JSONArray results;

    public NewsSearchService(String query){
        try{
            this.query = URLEncoder.encode(query, "UTF-8");
        }catch(UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
    }

    public JSONArray getResults() { return results; }

    @Override
    public void run() {
        URL url;
        boolean error = false;
        HttpURLConnection httpURLConnection = null;
        StringBuilder result = new StringBuilder();

        try{
            if(query.length() > 0){
                url = new URL("https://newsapi.org/v2/top-headlines?q="+ query +"&apiKey=74aca12306594201850ea6b45b6cf6d9&category=technology&country=gb");
            }else{
                url = new URL("https://newsapi.org/v2/top-headlines?q=phone&apiKey=74aca12306594201850ea6b45b6cf6d9&category=technology&country=gb");
            }
            httpURLConnection = (HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                result.append(line);
            }

            JSONObject jsonObject = new JSONObject(result.toString());
            if(jsonObject.has("status") && jsonObject.getString("status").equals("error")){
                error = true;
            }else{
                results = jsonObject.getJSONArray("articles");
            }

        }catch(Exception ex){
            ex.printStackTrace();
            results = null;
            error = true;
        }

        super.serviceCallComplete(error);
    }
}
