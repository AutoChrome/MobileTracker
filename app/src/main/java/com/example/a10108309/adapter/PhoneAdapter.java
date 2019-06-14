package com.example.a10108309.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;
import com.example.a10108309.phoneapplication.MainActivity;
import com.example.a10108309.phoneapplication.PhoneDetailsActivity;
import com.example.a10108309.phoneapplication.PhoneDisplayListener;
import com.example.a10108309.phoneapplication.R;
import com.example.a10108309.service.ImageSearchService;
import com.example.a10108309.service.ImageServiceListener;
import java.util.ArrayList;
import java.util.HashMap;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> implements ImageServiceListener {

    protected ArrayList<Phone> phoneList;
    protected HashMap<String, Bitmap> bitmapList;
    protected Context context;
    protected String imageSearchURL;
    private DatabaseHandler db;

    public class PhoneViewHolder extends RecyclerView.ViewHolder{
        public View mTextView;
        public PhoneViewHolder(@NonNull View v) {
            super(v);
            mTextView = v;
            final View viewHolder = v;
            final TextView tv = mTextView.findViewById(R.id.phone_view_holder_name);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!phoneList.get(getAdapterPosition()).getBrand().equalsIgnoreCase("error") && !phoneList.get(getAdapterPosition()).getBrand().equalsIgnoreCase("searching")){
                        int orientation = viewHolder.getResources().getConfiguration().orientation;
                        final Phone phone = phoneList.get(getAdapterPosition());
                        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                            PhoneDisplayListener context = (PhoneDisplayListener) getContext();
                            context.displayPhoneLandscape(phone);
                        }else{
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("phone", phone);
                            Intent intent = new Intent(context, PhoneDetailsActivity.class);
                            intent.putExtra("data", bundle);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                    }
                }
            });
        }
    }

    public PhoneAdapter(ArrayList<Phone> phoneList, Context context){
        this.phoneList = phoneList;
        bitmapList = new HashMap<String, Bitmap>();
        this.context = context;
        this.db = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phone_view_holder, viewGroup, false);
        PhoneViewHolder vh = new PhoneViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final PhoneViewHolder phoneViewholder, final int i) {
        TextView mText = (TextView) phoneViewholder.mTextView.findViewById(R.id.phone_view_holder_name);
        ImageView imageView = (ImageView) phoneViewholder.mTextView.findViewById(R.id.phone_view_holder_image);
        final ImageView favImage = (ImageView) phoneViewholder.mTextView.findViewById(R.id.fav_status);

        if(db.checkFavorite(phoneList.get(i).getDeviceName())){
            favImage.setBackground(context.getResources().getDrawable(R.drawable.ic_fav));
        }else {
            favImage.setBackground(context.getResources().getDrawable(R.drawable.ic_not_fav));
        }
        if(!phoneList.get(i).getBrand().equalsIgnoreCase("error") && !phoneList.get(i).getBrand().equalsIgnoreCase("searching")){
            favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(db.checkFavorite(phoneList.get(i).getDeviceName())){
                        db.deletePhone(phoneList.get(i).getDeviceName());
                        if(context instanceof MainActivity){
                            phoneList.remove(i);
                            notifyDataSetChanged();
                        }else{
                            favImage.setBackground(context.getResources().getDrawable(R.drawable.ic_not_fav));
                        }
                    }else{
                        db.insertPhone(phoneList.get(i));
                        favImage.setBackground(context.getResources().getDrawable(R.drawable.ic_fav));
                    }
                }
            });

            if(imageView.getDrawable() == null){
                if(bitmapList.containsKey(phoneList.get(i).getDeviceName())){
                    imageView.setImageBitmap(bitmapList.get(phoneList.get(i).getDeviceName()));
                }else{
                    Log.e("Download requested", "true");
                    ImageSearchService imageSearchService = new ImageSearchService(phoneList.get(i).getDeviceName(), context.getSharedPreferences("phoneCache", Context.MODE_PRIVATE), phoneViewholder);

                    Thread thread = new Thread(imageSearchService);
                    thread.run();
                    bitmapList.put(phoneList.get(i).getDeviceName(), imageSearchService.getResults());
                    Log.e("Bitmaplist size", bitmapList.size() + "");
                }
            }
        }

        mText.setText(phoneList.get(i).getDeviceName());
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    @Override
    public void imageServiceComplete(String url) {
        imageSearchURL = url;
    }

    public Context getContext(){
        return context;
    }
}
