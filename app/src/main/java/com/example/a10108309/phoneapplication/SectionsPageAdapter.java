package com.example.a10108309.phoneapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                PhoneFavoritedFragment phoneFragment = new PhoneFavoritedFragment();
                return phoneFragment;
            case 1:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Phone";
            case 1:
                return "News";
        }
        return null;
    }
}
