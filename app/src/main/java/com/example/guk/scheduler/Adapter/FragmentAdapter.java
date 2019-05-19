package com.example.guk.scheduler.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.guk.scheduler.DailyFragment;
import com.example.guk.scheduler.MonthlyFragment;
import com.example.guk.scheduler.WeeklyFragment;

/** 캘린더 액티비티 달.주.일 어댑터 **/
public class FragmentAdapter extends FragmentStatePagerAdapter {

    public MonthlyFragment fragment_Monthly;
    public WeeklyFragment fragment_Weekly;
    public DailyFragment fragment_Daily;

    public FragmentAdapter(FragmentManager fm)
    {
        super(fm);
        fragment_Monthly = new MonthlyFragment();
        fragment_Weekly = new WeeklyFragment();
        fragment_Daily = new DailyFragment();
    }
    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
            {
                return fragment_Monthly;
            }
            case 1:
            {
                return fragment_Weekly;
            }
            case 2:
            {
                return fragment_Daily;
            }
        }
        return null;
    }

    public int getCount()
    {
        return 3;
    }

    public void SetCalender()
    {
        fragment_Monthly.refreshMonthly();
        fragment_Weekly.refreshWeekly();
        fragment_Daily.refreshDaily();
    }

}
