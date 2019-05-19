package com.example.guk.scheduler;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guk.scheduler.DataManager.DateSingleton;
import com.example.guk.scheduler.Decorator.EventDecorator;
import com.example.guk.scheduler.Decorator.OneDayDecorator;
import com.example.guk.scheduler.Decorator.SaturdayDecorator;
import com.example.guk.scheduler.Decorator.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/** 주간 뷰 프래그먼트 **/
public class MonthlyFragment extends Fragment {

    Calendar selectedDate = Calendar.getInstance();

    MaterialCalendarView materialCalendarView;

    TextView selectedDateTv;

    FloatingActionButton MonthlyEditButton;

    CalenderActivity calenderActivity;

    ArrayList<CalendarDay> eventDays = new ArrayList<>();

    EventDecorator eventDecorator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_monthly, container, false);

        materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(DateSingleton.getInstance().selectedCal);
        materialCalendarView.setOnDateChangedListener(onDateSelectedListener);

        selectedDate.setTime(DateSingleton.getInstance().currentDate);

        selectedDateTv = (TextView)view.findViewById(R.id.selectDateTextView);
        setSelectedDateTv(DateSingleton.getInstance().selectedCal);


        // 주말 날짜 색 && 오늘 날짜 초록색으로 픽스
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());

        //일정이 있는 날짜 점 찍기
        eventDecorator = new EventDecorator(Color.RED, DateSingleton.getInstance().eventDates);
        materialCalendarView.addDecorator(eventDecorator);


        calenderActivity = (CalenderActivity)getActivity();

        materialCalendarView.setOnMonthChangedListener(onMonthChangedListener);

        MonthlyEditButton = (FloatingActionButton)view.findViewById(R.id.monthlyEditButton);
        MonthlyEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);

                intent.putExtra("time", DateSingleton.getInstance().selectedCal.getTimeInMillis());

                startActivityForResult(intent, 3000);
            }
        });

        return view;
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

                    eventCheckMonthly(DateSingleton.getInstance().selectedCal);

                    calenderActivity.refreshCalender();
                    break;
                }
            }
        }
        else if(resultCode == RESULT_CANCELED)
        {
        }
    }


    //다음 달 혹은 이전 달 전환 시
    OnMonthChangedListener onMonthChangedListener = new OnMonthChangedListener() {
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            if(calenderActivity.currentTabPosition != 0)
            {
                return;
            }

            if(DateSingleton.getInstance().selectedCal.get(Calendar.MONTH) != date.getMonth())
            {
                DateSingleton.getInstance().changeMonth(date.getCalendar());
            }

            calenderActivity.refreshCalender();

        }
    };


    //달력에서 날짜 선택 리스너
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

            setSelectedDateTv(date.getCalendar());
            DateSingleton.getInstance().selectedCal.setTime(date.getDate());
            calenderActivity.refreshCalender();
        }
    };

    void setSelectedDateTv(Calendar calendar)
    {
        String selectDay = "";
        selectDay +=  String.valueOf(calendar.get(Calendar.YEAR)) + ". ";
        selectDay += String.valueOf(calendar.get(Calendar.MONTH) + 1) + ". ";
        selectDay += String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        selectedDateTv.setText(selectDay);
    }

    /** 새로고침 **/
    public void refreshMonthly()
    {
        materialCalendarView.setCurrentDate(DateSingleton.getInstance().selectedCal);
        materialCalendarView.setSelectedDate(DateSingleton.getInstance().selectedCal);
        setSelectedDateTv(DateSingleton.getInstance().selectedCal);

    }

    /** 달력에 이벤트 유무 표시 업데이트 **/
    public void eventCheckMonthly(Calendar cal)
    {

        CalendarDay day = CalendarDay.from(cal.getTime());
        DateSingleton.getInstance().eventDates.add(day);

        eventDecorator.EventUpdate(DateSingleton.getInstance().eventDates);

        materialCalendarView.removeDecorator(eventDecorator);
        materialCalendarView.addDecorator(eventDecorator);
    }

    /** 매개변수로 넘어 온 날짜 이벤트 표시 지우기 **/
    public void eventRemoveMonthly(int year, int month, int day)
    {
        CalendarDay removeDay = CalendarDay.from(year, month, day);
        DateSingleton.getInstance().eventDates.remove(removeDay);

        eventDecorator.EventUpdate(DateSingleton.getInstance().eventDates);

        materialCalendarView.removeDecorator(eventDecorator);
        materialCalendarView.addDecorator(eventDecorator);


    }




}
