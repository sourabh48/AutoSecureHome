package com.example.soura.autosecurehome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by soura on 22-12-2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter
{

    public SectionPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                WeatherFragment weatherFragment=new WeatherFragment();
                return weatherFragment;
            case 1:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount()
    {
        return 2;
    }

    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Weather";
            case 1:
                return "Switches";
            default:
                return null;
        }
    }
}
