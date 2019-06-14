package com.example.a10108309.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.net.URL;

public class YoutubeImageService extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    public YoutubeImageService(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String imageURL = strings[0];
        Bitmap bimage = null;
        try{
            InputStream in = new URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);
        }catch(Exception e){

        }
        return bimage;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
