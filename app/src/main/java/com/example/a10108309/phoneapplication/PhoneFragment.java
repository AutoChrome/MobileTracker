package com.example.a10108309.phoneapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.a10108309.adapter.PhoneAdapter;
import com.example.a10108309.object.Phone;
import com.example.a10108309.object.PhoneImage;
import com.example.a10108309.service.AbstractService;
import com.example.a10108309.service.ImageSearchService;
import com.example.a10108309.service.PhoneSearchService;
import com.example.a10108309.service.ServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * PhoneFragment
 * This class is designed to display the results returned from the PhoneSearchService. It also uses the ImageSearchService to display the image.
 */

public class PhoneFragment extends Fragment implements ServiceListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;

    private Thread thread;
    ArrayList<Phone> phoneList;
    ArrayList<Phone> displayList;
    ArrayList<PhoneImage> imageList;

    private String brand, device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_fragment, container, false);
        phoneList = new ArrayList<Phone>();
        displayList = new ArrayList<Phone>();
        imageList = new ArrayList<PhoneImage>();
        progressBar = v.findViewById(R.id.fragment_progress_bar);
        this.brand = getArguments().getString("brand");
        this.device = getArguments().getString("device");
        doPhoneSearch(brand, device);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.phone_fragment_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhoneAdapter(displayList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(updateList);
        return v;
    }



    /**
     * Override the serviceComplete in the AbstractService.
     * @param abstractService
     */
    @Override
    public void serviceComplete(AbstractService abstractService) {
        //Check if the service had an error
        if(!abstractService.hasError()){
            PhoneSearchService phoneSearchService = null;
            //Try to cast the provided service to PhoneSearchService
            try{
                phoneSearchService = (PhoneSearchService)abstractService;
            }catch(Exception ex){
                ex.printStackTrace();
            }
            //Remove the place holder row from the displayList variable
            displayList.remove(0);

            progressBar.setVisibility(View.VISIBLE);
            for(int i = 0; i < phoneSearchService.getResults().length(); i++){
                try{
                    Phone phone = generatePhone(phoneSearchService.getResults().getJSONObject(i));
                    phoneList.add(phone);
                    if(i < 10){
                        displayList.add(phone);
                    }
                }catch(JSONException ex){
                    ex.printStackTrace();
                    phoneList.add(new Phone("Error", "error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error", "Error"));
                }
            }

            if(phoneList.size() < 1 && displayList.size() < 1) {
                displayList.remove(0);
                displayList.add(new Phone("No phones found.", "error", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found."));
            }

            progressBar.setVisibility(View.INVISIBLE);
        }else{
            if(phoneList.size() < 1){
                displayList.remove(0);
                displayList.add(new Phone("No phones found.", "error", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found.", "No phones found."));
            }
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("phoneCache", Context.MODE_PRIVATE);
        Map<String, ?> keys = sharedPreferences.getAll();
        for(Map.Entry<String, ?> entry : keys.entrySet()){
            imageList.add(new PhoneImage(entry.getKey(), entry.getValue().toString()));
        }

        mAdapter = new PhoneAdapter(displayList, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void doPhoneSearch(@Nullable String brand, @Nullable String device) {
        displayList.add(new Phone("Searching... ", "searching", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... ", "Searching... "));
        PhoneSearchService phoneSearchService;
        if(brand == null){
            phoneSearchService = new PhoneSearchService(null, device);
        }else{
            phoneSearchService = new PhoneSearchService(brand, device);
        }

        phoneSearchService.addListener(this);

        thread = new Thread(phoneSearchService);

        thread.start();
    }

    private RecyclerView.OnScrollListener updateList = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(!mRecyclerView.canScrollVertically(1)){
                progressBar.setVisibility(View.VISIBLE);
                progressBar.bringToFront();
                int x = 0;
                for (int i = displayList.size(); i < phoneList.size(); i++){
                    if(x < 10){
                        displayList.add(phoneList.get(i));
                    }
                }

                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    public Phone generatePhone(JSONObject jsonObject){
        String deviceName, brand, cpu, battery, resolution, chargerPort, mainCamera, size, weight, dimensions, screenType, nfcEnabled, internal;
        if(jsonObject.has("DeviceName")){
            try{
                deviceName = jsonObject.getString("DeviceName");
            }catch(Exception ex){
                deviceName = "False";
            }
        }else{deviceName = "False";}

        if(jsonObject.has("Brand")){
            try{
                brand = jsonObject.getString("Brand");
            }catch(Exception ex){
                brand = "False";
            }
        }else{brand = "False";}

        if(jsonObject.has("cpu")){
            try{
                cpu = jsonObject.getString("cpu");
            }catch(Exception ex){
                cpu = "False";
            }
        }else{cpu = "False";}

        if(jsonObject.has("battery")){
            try{
                battery = jsonObject.getString("battery");
            }catch(Exception ex){
                battery = "False";
            }
        }else{battery = "False";}

        if(jsonObject.has("resolution")){
            try{
                resolution = jsonObject.getString("resolution");
            }catch(Exception ex){
                resolution = "False";
            }
        }else{resolution = "False";}

        if(jsonObject.has("usb")){
            try{
                chargerPort = jsonObject.getString("usb");
            }catch(Exception ex){
                chargerPort = "False";
            }
        }else{chargerPort = "False";}

        if(jsonObject.has("primary_")){
            try{
                mainCamera = jsonObject.getString("primary_");
            }catch(Exception ex){
                mainCamera = "False";
            }
        } else{ mainCamera = "False"; }

        if(jsonObject.has("size")){
            try{
                size = jsonObject.getString("size");
            }catch(Exception ex){
                size = "False";
            }
        }else{size = "False";}

        if(jsonObject.has("weight")){
            try{
                weight = jsonObject.getString("weight");
            }catch(Exception ex){
                weight = "False";
            }
        }else{weight = "False";}

        if(jsonObject.has("dimensions")){
            try{
                dimensions = jsonObject.getString("dimensions");
            }catch(Exception ex){
                dimensions = "False";
            }
        }else{dimensions = "False";}

        if(jsonObject.has("type")){
            try{
                screenType = jsonObject.getString("type");
            }catch(Exception ex){
                screenType = "False";
            }
        }else{screenType = "False";}

        if(jsonObject.has("nfc")){
            try{
                nfcEnabled = jsonObject.getString("nfc");
            }catch(Exception ex){
                nfcEnabled = "False";
            }
        }else{nfcEnabled = "False";}

        if(jsonObject.has("internal")){
            try{
                internal = jsonObject.getString("internal");
            }catch(Exception ex){
                internal = "False";
            }
        }else{internal = "False";}

        Phone phone = new Phone(deviceName, brand, cpu, battery, resolution, chargerPort, mainCamera, size, weight, dimensions, screenType, nfcEnabled, internal);
        return phone;
    }
}
