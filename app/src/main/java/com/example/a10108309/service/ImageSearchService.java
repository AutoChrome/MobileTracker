package com.example.a10108309.service;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.example.a10108309.adapter.PhoneAdapter;
import com.example.a10108309.object.Phone;
import com.example.a10108309.phoneapplication.MainActivity;
import com.example.a10108309.phoneapplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ImageSearchService extends AbstractService{

    Bitmap bitmapResult;
    PhoneAdapter.PhoneViewHolder vh;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private String query;
    private String results;
    private String key = "AIzaSyC5lxcxiggGntoNXFowCGjxRLOHML_QgQI";
    private String cx = "017149576032363357391:tmpycfy7edm";

    public ImageSearchService(String query, SharedPreferences sharedPreferences, PhoneAdapter.PhoneViewHolder vh){
        try{
            this.sharedPreferences = sharedPreferences;
            this.query = URLEncoder.encode(query, "UTF-8");
            this.vh = vh;
        }catch(UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
    }

    public ImageSearchService(String query, SharedPreferences sharedPreferences, ImageView imageView){
        try{
            this.sharedPreferences = sharedPreferences;
            this.query = URLEncoder.encode(query, "UTF-8");
            this.imageView = imageView;
        }catch(UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
    }

    public Bitmap getResults(){
        return this.bitmapResult;
    }

    @Override
    public void run() {
        GetImageFromInternet getImageFromInternet = new GetImageFromInternet();

        getImageFromInternet.execute(query);
    }

    private class GetImageFromInternet extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String query = strings[0];
            URL url;
            boolean error = false;
            HttpURLConnection httpURLConnection = null;
            StringBuilder result = new StringBuilder();


            try{
                if(sharedPreferences.getString(query, null) == null){
                    Log.e("Query performed", "true");
                    url = new URL("https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&num=1&start=1&searchType=image&q=" + query);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.e("Response error ", httpURLConnection.getResponseMessage());
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if(jsonObject.has("items") && jsonObject.getString("items").length() < 1){
                        error = true;
                    }else{
                        JSONArray items = jsonObject.getJSONArray("items");
                        JSONObject item = items.getJSONObject(0);
                        if(item.has("link")){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(query, item.getString("link"));
                            editor.apply();
                            results = item.getString("link");

                        }
                        //results = jsonArray;
                    }
                }else{
                    Log.e("Query performed", "false");
                    results = sharedPreferences.getString(query, null);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                results = null;
                error = true;
            }

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            DownloadImageFromInternet downloadImageFromInternet = new DownloadImageFromInternet();

            downloadImageFromInternet.execute(s);
        }
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try{
                InputStream in = new URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            }catch(Exception e){
                e.printStackTrace();
            }
            return bimage;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(vh != null){
                ImageView imageView = vh.mTextView.findViewById(R.id.phone_view_holder_image);

                imageView.setImageBitmap(bitmap);
            }else{
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
