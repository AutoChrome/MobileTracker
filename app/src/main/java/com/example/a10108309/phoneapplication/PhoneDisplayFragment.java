package com.example.a10108309.phoneapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a10108309.database.DatabaseHandler;
import com.example.a10108309.object.Phone;
import com.example.a10108309.service.ImageSearchService;

public class PhoneDisplayFragment extends Fragment {

    RelativeLayout content_one, content_two, header_one_relative_layout, header_two_relative_layout;
    TextView header_one, header_two;
    TextView devicename_tv, dimensions_tv, weight_tv, size_tv, camera_tv, cpu_tv,ram_tv, resolution_tv, chargerPort_tv, nfc_tv, screenType_tv;
    ImageView phoneImage, fav_icon, header_one_chevron, header_two_chevron;

    public PhoneDisplayFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_details_fragment, container, false);

        final DatabaseHandler db = new DatabaseHandler(getContext());

        Bundle bundle = getArguments();

        final Phone phone = bundle.getParcelable("phone");

        header_one = (TextView) v.findViewById(R.id.header_one);
        header_two = (TextView) v.findViewById(R.id.header_two);
        header_one_relative_layout = (RelativeLayout) v.findViewById(R.id.header_one_relative_layout);
        header_two_relative_layout = (RelativeLayout) v.findViewById(R.id.header_two_relative_layout);

        header_one_chevron = v.findViewById(R.id.header_one_chevron);
        header_two_chevron = v.findViewById(R.id.header_two_chevron);

        dimensions_tv = (TextView) v.findViewById(R.id.dimensions_tv);
        weight_tv = (TextView) v.findViewById(R.id.weight_tv);
        size_tv = (TextView) v.findViewById(R.id.size_tv);
        camera_tv = (TextView) v.findViewById(R.id.camera_tv);
        cpu_tv = (TextView) v.findViewById(R.id.cpu_tv);
        ram_tv = (TextView) v.findViewById(R.id.ram_tv);
        resolution_tv = (TextView) v.findViewById(R.id.resolution_tv);
        chargerPort_tv = (TextView) v.findViewById(R.id.chargerPort_tv);
        nfc_tv = (TextView) v.findViewById(R.id.nfc_tv);
        screenType_tv = (TextView) v.findViewById(R.id.screenType_tv);
        phoneImage = (ImageView) v.findViewById(R.id.phone_image);
        devicename_tv = (TextView) v.findViewById(R.id.phone_deviceName);
        fav_icon = (ImageView) v.findViewById(R.id.fav_icon);

        content_one = (RelativeLayout) v.findViewById(R.id.content_one);
        content_two = (RelativeLayout) v.findViewById(R.id.content_two);

        devicename_tv.setText(phone.getDeviceName());

        if(db.checkFavorite(phone.getDeviceName())){
            fav_icon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_fav));
        }else{
            fav_icon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_not_fav));
        }

        fav_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.checkFavorite(phone.getDeviceName())){
                    db.deletePhone(phone.getDeviceName());
                    fav_icon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_not_fav));
                }else{
                    db.insertPhone(phone);
                    fav_icon.setBackground(getContext().getResources().getDrawable(R.drawable.ic_fav));
                }
            }
        });

        ImageSearchService imageSearchService = new ImageSearchService(phone.getDeviceName(), getContext().getSharedPreferences("phoneCache", Context.MODE_PRIVATE), phoneImage);
        imageSearchService.run();

        dimensions_tv.setText(phone.getDimensions());
        weight_tv.setText(phone.getWeight());
        size_tv.setText(phone.getSize());
        camera_tv.setText(phone.getMainCamera());
        cpu_tv.setText(phone.getCpu());
        ram_tv.setText(phone.getInternal());
        resolution_tv.setText(phone.getResolution());
        chargerPort_tv.setText(phone.getChargerPort());
        nfc_tv.setText(phone.getNfcEnabled());
        screenType_tv.setText(phone.getScreenType());

        content_one.setVisibility(View.GONE);
        content_two.setVisibility(View.GONE);

        header_one_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content_one.getVisibility() == View.VISIBLE){
                    header_one_chevron.setBackground(getContext().getResources().getDrawable(R.drawable.ic_down));
                    content_one.setVisibility(View.GONE);
                }else{
                    header_one_chevron.setBackground(getContext().getResources().getDrawable(R.drawable.ic_up));
                    content_one.setAlpha(0.0f);
                    content_one.setVisibility(View.VISIBLE);
                    content_one.animate().translationY(content_one.getHeight()).alpha(1.f).setListener(null);
                    content_one.animate().translationY(0);
                }
            }
        });

        header_two_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content_two.getVisibility() == View.VISIBLE){
                    header_two_chevron.setBackground(getContext().getResources().getDrawable(R.drawable.ic_down));
                    content_two.setVisibility(View.GONE);
                }else{
                    header_two_chevron.setBackground(getContext().getResources().getDrawable(R.drawable.ic_up));
                    content_two.setAlpha(0.0f);
                    content_two.setVisibility(View.VISIBLE);
                    content_two.animate().translationY(content_one.getHeight()).alpha(1.f).setListener(null);
                    content_two.animate().translationY(0);
                }
            }
        });

        FragmentManager fragmentManager = getFragmentManager();

        Bundle ytBundle = new Bundle();

        ytBundle.putString("query", phone.getDeviceName());

        Fragment fragment = new YoutubeFragment();

        fragment.setArguments(ytBundle);

        if(fragmentManager.findFragmentByTag("youtubeFragment") == null){
            fragmentManager.beginTransaction().add(R.id.youtube_frame_layout, fragment, "youtubeFragment").commit();
        }

        return v;
    }
}
