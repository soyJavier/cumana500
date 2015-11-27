package com.cumana.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Javier on 18-11-2015.
 */
public class PagerAdaptersImage extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdaptersImage(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle params = new Bundle();
        params.putInt("position",position);

        //fragment_image img = new fragment_image();
        //img.setArguments(params);

        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
