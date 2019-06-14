package com.example.a10108309.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Article;
import com.example.a10108309.phoneapplication.MainActivity;
import com.example.a10108309.phoneapplication.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<Article> articles;
    private HashMap<String, Bitmap> bitmapList;
    private DatabaseHandler db;
    private Context context;

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public View articleView;
        public NewsViewHolder(@NonNull View v) {
            super(v);
            articleView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!articles.get(getAdapterPosition()).getTitle().equalsIgnoreCase("Searching...")){
                        String url = articles.get(getAdapterPosition()).getUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try{
                            intent.setData(Uri.parse(url));
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public NewsAdapter(ArrayList<Article> articles, Context context){
        this.articles = articles;
        this.bitmapList = new HashMap<String, Bitmap>();
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_view_holder, viewGroup, false);
        NewsViewHolder vh = new NewsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, final int i) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        Date parsedDate = null;
        try{
            parsedDate = format.parse(articles.get(i).getPublishedAt());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        TextView tv_title = (TextView)newsViewHolder.articleView.findViewById(R.id.news_view_holder_card_view_title);
        TextView tv_source = (TextView)newsViewHolder.articleView.findViewById(R.id.news_view_holder_source);
        TextView tv_publishDate = (TextView)newsViewHolder.articleView.findViewById(R.id.news_view_holder_publish_date);
        ImageView imageView = (ImageView)newsViewHolder.articleView.findViewById(R.id.news_view_holder_card_view_image);
        final ImageView fav_icon = (ImageView)newsViewHolder.articleView.findViewById(R.id.news_view_holder_fav_image_holder);
        tv_title.setText(articles.get(i).getTitle());
        tv_source.setText("Source: " + articles.get(i).getSource());
        if(parsedDate != null){
            tv_publishDate.setText(parsedDate.toString());
        }
        if(db.checkArticleFavorite(articles.get(i).getTitle())){
            fav_icon.setBackground(context.getResources().getDrawable(R.drawable.ic_bookmarked));
        }else{
            fav_icon.setBackground(context.getResources().getDrawable(R.drawable.ic_not_bookmark));
        }

        fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.checkArticleFavorite(articles.get(i).getTitle())){
                    db.deleteArticle(articles.get(i).getTitle());
                    if(context instanceof MainActivity){
                        articles.remove(i);
                        notifyDataSetChanged();
                    }else{
                        fav_icon.setBackground(context.getResources().getDrawable(R.drawable.ic_not_bookmark));
                    }
                }else{
                    db.insertArticle(articles.get(i));
                    fav_icon.setBackground(context.getResources().getDrawable(R.drawable.ic_bookmarked));
                }
            }
        });

        if(bitmapList.get(articles.get(i).getTitle()) == null){
            if(!articles.get(i).getImageURL().equalsIgnoreCase("searching")){
                DownloadImageFromInternet downloadImageFromInternet = new DownloadImageFromInternet(imageView, articles.get(i).getTitle());
                downloadImageFromInternet.execute(articles.get(i).getImageURL());
            }
        }else{
            imageView.setImageBitmap(bitmapList.get(articles.get(i).getTitle()));
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>{

        private ImageView imageView;
        public Bitmap bitmap;
        private String articleName;

        public DownloadImageFromInternet(ImageView imageView, String articleName){
            this.imageView = imageView;
            this.articleName = articleName;
        }
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
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            this.bitmap = bitmap;
            bitmapList.put(articleName, bitmap);
        }
    }

}
