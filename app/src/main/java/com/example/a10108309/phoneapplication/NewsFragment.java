package com.example.a10108309.phoneapplication;

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

import com.example.a10108309.adapter.NewsAdapter;
import com.example.a10108309.adapter.PhoneAdapter;
import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Article;
import com.example.a10108309.service.AbstractService;
import com.example.a10108309.service.NewsSearchService;
import com.example.a10108309.service.PhoneSearchService;
import com.example.a10108309.service.ServiceListener;

import org.json.JSONException;

import java.util.ArrayList;

public class NewsFragment extends Fragment implements ServiceListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler db;

    private String query;

    private Thread thread;
    ArrayList<Article> articles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_fragment, container, false);
        db = new DatabaseHandler(getContext());
        articles = new ArrayList<Article>();
        if(getContext() instanceof MainActivity){
            articles = db.getArticles();
        }else{
            if(getArguments() != null){
                doSearch(getArguments().getString("query"));
            }else{
                doSearch("");
            }
        }
        mRecyclerView = v.findViewById(R.id.news_fragment_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(articles, getContext());
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void serviceComplete(AbstractService abstractService) {
        if(!abstractService.hasError()){
            NewsSearchService newsSearchService = (NewsSearchService)abstractService;
            articles.remove(0);
            for(int i = 0; i < newsSearchService.getResults().length(); i++){
                try{
                    articles.add(new Article(newsSearchService.getResults().getJSONObject(i).getString("title"), newsSearchService.getResults().getJSONObject(i).getString("publishedAt"), newsSearchService.getResults().getJSONObject(i).getString("url"), newsSearchService.getResults().getJSONObject(i).getString("urlToImage"), newsSearchService.getResults().getJSONObject(i).getJSONObject("source").getString("name")));
                }catch(JSONException ex){
                    ex.printStackTrace();
                    articles.add(new Article("Error", "error", "error", "error", "error"));
                }
            }
        }
        mAdapter = new NewsAdapter(articles, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void doSearch(String query){
        articles.add(new Article("Searching...", "searching", "searching", "searching", "searching"));

        NewsSearchService newsSearchService = new NewsSearchService(query);

        newsSearchService.addListener(this);

        thread = new Thread(newsSearchService);

        thread.start();
    }
}
