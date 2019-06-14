package com.example.a10108309.phoneapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10108309.service.YoutubeImageService;
import com.example.a10108309.service.YoutubeService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class YoutubeFragment extends Fragment {

    private JSONArray results;
    private String query;
    private ViewGroup youtube_scroll_view, youtube_linear_layout;

    public YoutubeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.youtube_fragment, container, false);

        this.query = getArguments().getString("query");

        youtube_scroll_view = (ViewGroup) v.findViewById(R.id.youtube_horizontal_scroll);
        youtube_linear_layout = (ViewGroup) v.findViewById(R.id.youtube_linear_layout);

        YoutubeService youtubeService = new YoutubeService(query, this);

        try{
            this.results = youtubeService.execute(query).get();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return v;
    }

    public void serviceComplete(JSONArray results){
        this.results = results;

        try{
            for(int i = 0; i < results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                jsonObject = jsonObject.getJSONObject("snippet");

                createCard(getLayoutInflater(), youtube_linear_layout, jsonObject, results.getJSONObject(i).getJSONObject("id").getString("videoId"));

                Log.e("jsonObject", jsonObject.toString());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void createCard(LayoutInflater inflater, ViewGroup container, JSONObject video, final String videoId){
        View v = inflater.inflate(R.layout.youtube_card, container, false);

        ImageView youtube_thumbnail = v.findViewById(R.id.youtube_thumbnail);
        TextView youtube_title = v.findViewById(R.id.youtube_title);
        TextView youtube_channel = v.findViewById(R.id.youtube_channel);

        try{
            youtube_title.setText(video.getString("title"));
            youtube_channel.setText(video.getString("channelTitle"));
            String imageUrl = video.getJSONObject("thumbnails").getJSONObject("high").getString("url");
            YoutubeImageService youtubeImageService = new YoutubeImageService(youtube_thumbnail);
            youtubeImageService.execute(imageUrl);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + videoId));
                intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));

                PackageManager packageManager = getContext().getPackageManager();
                List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, 0);

                if(infos.size() > 0){
                    getContext().startActivity(intent);
                }else{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + videoId));
                    startActivity(intent);
                }
            }
        });

        container.addView(v);
    }
}
