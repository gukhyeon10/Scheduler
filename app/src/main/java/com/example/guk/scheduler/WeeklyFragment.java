package com.example.guk.scheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guk.scheduler.Adapter.ListViewCustomAdapter;
import com.example.guk.scheduler.DataManager.DataBaseOpenHelper;
import com.example.guk.scheduler.DataManager.DateSingleton;
import com.example.guk.scheduler.Decorator.OneDayDecorator;
import com.example.guk.scheduler.Decorator.SaturdayDecorator;
import com.example.guk.scheduler.Decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/** 주간 뷰 프래그먼트 **/
public class WeeklyFragment extends Fragment{

    MaterialCalendarView materialCalendarWeekView;

    CalenderActivity calenderActivity;

    FloatingActionButton WeeklyEditButton;

    //일, 월, 화, 수, 목, 금, 토  -->  리스트뷰
    ListView sunListView, monListView, tueListView, wedListView, thuListView, friListView, satListView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weekly, container, false);

        materialCalendarWeekView = (MaterialCalendarView)view.findViewById(R.id.calendarWeekView);

        materialCalendarWeekView.setSelectedDate(DateSingleton.getInstance().selectedCal);

        materialCalendarWeekView.setOnDateChangedListener(onDateSelectedListener);
        materialCalendarWeekView.setOnMonthChangedListener(onMonthChangedListener);

        materialCalendarWeekView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());

        calenderActivity = (CalenderActivity)getActivity();

        //일정 등록 버튼 및 클릭 리스너
        WeeklyEditButton = (FloatingActionButton)view.findViewById(R.id.weeklyEditButton);
        WeeklyEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);

                intent.putExtra("time", DateSingleton.getInstance().selectedCal.getTimeInMillis());

                startActivityForResult(intent, 3000);
            }
        });

        //리스트 뷰 등록 및 롱클릭 리스너
        sunListView= (ListView)view.findViewById(R.id.weekly_Sun);
        monListView= (ListView)view.findViewById(R.id.weekly_Mon);
        tueListView= (ListView)view.findViewById(R.id.weekly_Tue);
        wedListView= (ListView)view.findViewById(R.id.weekly_Wed);
        thuListView= (ListView)view.findViewById(R.id.weekly_Thu);
        friListView= (ListView)view.findViewById(R.id.weekly_Fri);
        satListView= (ListView)view.findViewById(R.id.weekly_Sat);

        sunListView.setOnItemLongClickListener(listViewItemLongClick);
        monListView.setOnItemLongClickListener(listViewItemLongClick);
        tueListView.setOnItemLongClickListener(listViewItemLongClick);
        wedListView.setOnItemLongClickListener(listViewItemLongClick);
        thuListView.setOnItemLongClickListener(listViewItemLongClick);
        friListView.setOnItemLongClickListener(listViewItemLongClick);
        satListView.setOnItemLongClickListener(listViewItemLongClick);

        //주간 뷰 새로고침
        refreshWeekly();

        return view;
    }

    //주간 스케줄 로드
    void ScheduleLoad()
    {
        Calendar date = materialCalendarWeekView.getSelectedDate().getCalendar();
        int thisDay = date.get(Calendar.DAY_OF_WEEK);
        date.add(Calendar.DATE, -thisDay + 1);
        setListView(sunListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(monListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(tueListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(wedListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(thuListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(friListView, date);
        date.add(Calendar.DATE,  +1);
        setListView(satListView, date);

    }

    //리스트 뷰 셋팅
    void setListView(ListView listView, Calendar date)
    {
        List<ScheduleInfo> scheduleList = new ArrayList<ScheduleInfo>();
        ListViewCustomAdapter customAdapter = new ListViewCustomAdapter(getActivity(), R.layout.listview_weekly_item, scheduleList);
        listView.setAdapter(customAdapter);

        DataBaseOpenHelper helper;
        helper = new DataBaseOpenHelper(getActivity(), DataBaseOpenHelper.tableName, null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseOpenHelper.tableName + " WHERE year = " +date.get(Calendar.YEAR) + " AND month = " + date.get(Calendar.MONTH) + " AND day = " + date.get(Calendar.DATE);
        Cursor cursor = database.rawQuery(sql, null);

        //스케줄이 있으면 리스트 뷰에 추가
        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                ScheduleInfo newSchedule = new ScheduleInfo();

                newSchedule.setSchedule(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
                scheduleList.add(newSchedule);
            }

            listView.setAdapter(customAdapter);
        }

        cursor.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case 3000:
                {
                    int year = data.getIntExtra("Year", 2000);
                    int month = data.getIntExtra("Month", 1);
                    int day = data.getIntExtra("Day", 1);

                    DateSingleton.getInstance().selectedCal.set(year, month, day);


                    calenderActivity.refreshCalender();

                    calenderActivity.fragmentAdapter.fragment_Monthly.eventCheckMonthly(DateSingleton.getInstance().selectedCal);
                    break;
                }
            }
        }
        else if(resultCode == RESULT_CANCELED)
        {

        }
    }

    OnDateSelectedListener onDateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            //선택한 날짜를 한번 더 클릭하면
            if(date.getDate().getYear() == DateSingleton.getInstance().selectedCal.getTime().getYear())
            {
                if(date.getDate().getMonth() == DateSingleton.getInstance().selectedCal.getTime().getMonth())
                {
                    if(date.getDate().getDate() == DateSingleton.getInstance().selectedCal.getTime().getDate())
                    {
                        calenderActivity.viewPager.setCurrentItem(2);
                    }
                }
            }


            DateSingleton.getInstance().selectedCal.setTime(date.getDate());
            calenderActivity.refreshCalender();
        }
    };

    AdapterView.OnItemLongClickListener listViewItemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {

            final DataBaseOpenHelper helper;
            helper = new DataBaseOpenHelper(getActivity(), DataBaseOpenHelper.tableName, null, 1);
            final SQLiteDatabase database = helper.getWritableDatabase();

            final TextView titleTv = view.findViewById(R.id.weekly_item_title);
            final LinearLayout bgLayout = view.findViewById(R.id.weekly_item_color);

            Calendar date = materialCalendarWeekView.getSelectedDate().getCalendar();
            int thisDay = date.get(Calendar.DAY_OF_WEEK);
            switch (parent.getId())
            {
                case R.id.weekly_Sun:
                {
                    date.add(Calendar.DATE, -thisDay+1);
                    break;
                }
                case R.id.weekly_Mon:
                {
                    date.add(Calendar.DATE, -thisDay + 2);
                    break;
                }
                case R.id.weekly_Tue:
                {
                    date.add(Calendar.DATE, -thisDay + 3);
                    break;
                }
                case R.id.weekly_Wed:
                {
                    date.add(Calendar.DATE, -thisDay + 4);
                    break;
                }
                case R.id.weekly_Thu:
                {
                    date.add(Calendar.DATE, -thisDay + 5);
                    break;
                }
                case R.id.weekly_Fri:
                {
                    date.add(Calendar.DATE, -thisDay + 6);
                    break;
                }
                case R.id.weekly_Sat:
                {
                    date.add(Calendar.DATE, -thisDay + 7);
                    break;
                }

            }

            final int year = date.get(Calendar.YEAR);
            final int month = date.get(Calendar.MONTH);
            final int day = date.get(Calendar.DATE);

            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setMessage(titleTv.getText().toString());
            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    ColorDrawable color = (ColorDrawable)bgLayout.getBackground();
                    int bgColor = color.getColor();

                    helper.DeleteData(database, year, month, day, titleTv.getText().toString(), bgColor);
                    calenderActivity.refreshCalender();

                    if(parent.getCount() <= 1)
                    {
                        calenderActivity.fragmentAdapter.fragment_Monthly.eventRemoveMonthly(year, month, day);

                    }
                }
            });

            alert.show();
            return false;
        }
    };

    //이전 or 다음 주로 넘겨질때 호출되는 리스너
    OnMonthChangedListener onMonthChangedListener = new OnMonthChangedListener() {
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            //달력 탭이 아니면 호출 금지
            if(calenderActivity.currentTabPosition != 1)
            {
                return;
            }

            if(date.getMonth() != DateSingleton.getInstance().selectedCal.get(Calendar.MONTH) || date.getDay() != DateSingleton.getInstance().selectedCal.get(Calendar.DATE))
            {
                DateSingleton.getInstance().changeWeek(date.getCalendar());
                calenderActivity.refreshCalender();
            }

        }
    };

    /** 주간 뷰 새로고침 **/
    public void refreshWeekly()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateSingleton.getInstance().selectedCal.getTime());

        /** 녹스애뮬레이터에서만 주간 탭에서 한 주 전이 셋팅됨..   녹스에서는 한 주 더해주고 그 외에는 주석 처리 **/
         //cal.add(Calendar.DATE, +7);

        materialCalendarWeekView.setCurrentDate(cal);
        materialCalendarWeekView.setSelectedDate(DateSingleton.getInstance().selectedCal);

        ScheduleLoad();
    }
}
