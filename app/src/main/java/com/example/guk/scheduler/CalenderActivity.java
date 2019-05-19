package com.example.guk.scheduler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.guk.scheduler.Adapter.FragmentAdapter;
import com.example.guk.scheduler.DataManager.DataBaseOpenHelper;
import com.example.guk.scheduler.DataManager.DateSingleton;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/** 캘린더 뷰 액티비티 **/
public class CalenderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    int currentTabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender);

        eventUpLoad();

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.addTab(tabLayout.newTab().setText("Monthly"), 0, true);
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("Daily"), 2);

        tabLayout.addOnTabSelectedListener(pagerListener);

        currentTabPosition = 0;  // 달력으로 시작

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    //탭 리스너
    TabLayout.OnTabSelectedListener pagerListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());

            //캘린더뷰의 월단위 혹은 주단위로 바뀔때 월간과 주간이 같이 리스너가 중복되어 호출되어 각각 플로우를 다르게 타려고 추가한 코드
            //하... 이것땜에 며칠을 고생했냐..
            switch (tab.getPosition())
            {
                case 0: //월간
                {
                    DateSingleton.getInstance().isWeekly = false;
                    currentTabPosition = 0;
                    break;
                }
                case 1: //주간
                {
                    DateSingleton.getInstance().isWeekly = true;
                    currentTabPosition = 1;
                    break;
                }
                case 2:  //일일
                {
                    currentTabPosition = 2;
                    break;
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    /** 달.주.일  새로고침 **/
    public void refreshCalender()
    {
        fragmentAdapter.SetCalender();
    }

    /** 디비에서 스케줄 업로드**/
    void eventUpLoad()
    {
        DataBaseOpenHelper helper;
        helper = new DataBaseOpenHelper(this, "Schedule", null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();

        String sql = "SELECT * FROM Schedule";
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                int year = cursor.getInt(0);
                int month = cursor.getInt(1);
                int day = cursor.getInt(2);

                CalendarDay calendarDay  = CalendarDay.from(year, month, day);

                if(!DateSingleton.getInstance().eventDates.contains(calendarDay))
                {
                    DateSingleton.getInstance().eventDates.add(calendarDay);
                }
            }
        }

    }
}
