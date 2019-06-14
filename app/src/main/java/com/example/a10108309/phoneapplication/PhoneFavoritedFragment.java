package com.example.a10108309.phoneapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a10108309.adapter.PhoneAdapter;
import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;
import com.example.a10108309.object.PhoneImage;
import com.example.a10108309.service.ImageSearchService;

import java.util.ArrayList;

public class PhoneFavoritedFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Phone> phoneList;
    ArrayList<Phone> displayList;
    ArrayList<PhoneImage> imageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DatabaseHandler db = new DatabaseHandler(this.getContext());
        View v = inflater.inflate(R.layout.phone_favorite_fragment, container, false);
        phoneList = db.getPhones();
        displayList = phoneList;

        imageList = new ArrayList<PhoneImage>();

        mRecyclerView = (RecyclerView)v.findViewById(R.id.phone_fragment_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PhoneAdapter(displayList, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(updateList);
        return v;
    }

    private RecyclerView.OnScrollListener updateList = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(!mRecyclerView.canScrollVertically(1)){
                int x = 0;
                for (int i = displayList.size(); i < phoneList.size(); i++){
                    if(x < 10){
                        displayList.add(phoneList.get(i));
                        x++;
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };
}
