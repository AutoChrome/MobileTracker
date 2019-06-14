package com.example.a10108309.service;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PhoneSearchService extends AbstractService {

    /**
     *
     * Used to get information on a phone from the FonoAPI service.
     *
     */

    private String query;
    private String device = "device=";
    private String token = "token=4e7f47b5b3995372ced2c89e7776c7093b5c85edf2ba6de6";
    private String brand = "brand=";
    private String type = "";
    private JSONArray results;

    /**
     * Constructor for PhoneSearchService. Requires a string and will perform a query based on the string.
     * @param brand The query that wil be performed on the API
     */
    public PhoneSearchService(@Nullable String brand, String device){
        if(brand != null){
            this.brand += brand;
        }else{
            this.brand = null;
        }
        if(device.equalsIgnoreCase("getLatest")){
            this.type = "getLatest";
        }else{
            this.type = "device";
            this.device += device;
        }
    }

    /**
     *Gets the results of the query and returns them.
     * @return Returns JSONArray
     */
    public JSONArray getResults(){
        return results;
    }

    /**
     * When creating this object and start has been executed, run this code. Code queries the API and returns any found data and stores it into the results variable.
     */
    @Override
    public void run() {
        URL url;
        boolean error = false;
        HttpURLConnection httpURLConnection = null;
        StringBuilder result = new StringBuilder();

        try{
            if(type.equalsIgnoreCase("getLatest")){
                url = new URL("https://fonoapi.freshpixl.com/v1/getlatest?" + token);
            }else{
                if(brand == "brand="){
                    url = new URL("https://fonoapi.freshpixl.com/v1/getdevice?" + token + "&" + device);
                }else{
                    url = new URL("https://fonoapi.freshpixl.com/v1/getdevice?" + token + "&" + brand + "&" + device);
                }
            }
            Log.e("Url", url.toString());
            httpURLConnection = (HttpURLConnection)url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                result.append(line);
            }

            JSONArray jsonArray = new JSONArray(result.toString());
            JSONObject jsonObject = jsonArray.toJSONObject(jsonArray);
            if(jsonObject.has("status") && jsonObject.getString("status").equals("error")){
                error = true;
            }else{
                results = jsonArray;
            }

        }catch(Exception ex){
            ex.printStackTrace();
            results = null;
            error = true;
        }

        super.serviceCallComplete(error);
    }
}
