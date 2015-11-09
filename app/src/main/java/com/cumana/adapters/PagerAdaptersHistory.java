package com.cumana.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cumana.history.fragment_history;
import com.cumana.history.fragment_persons;

/**
 * Created by Javier on 23-10-2015.
 */
public class PagerAdaptersHistory extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdaptersHistory(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                fragment_history history = new fragment_history();
                return history;
            case 1:
                fragment_persons persons = new fragment_persons();
                return persons;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
