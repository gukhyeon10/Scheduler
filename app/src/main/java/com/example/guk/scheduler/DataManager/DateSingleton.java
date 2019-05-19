package com.example.guk.scheduler.DataManager;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/** 날짜 싱글톤)) 오늘 날짜 or 지금 선택된 날짜 or 이벤트 유무 리스트   관리싱글톤  **/
public class DateSingleton {
    private static final DateSingleton instance = new DateSingleton();
    public Date currentDate;
    public long currentTime;
    public Calendar selectedCal;
    public Calendar todayCal;

    public ArrayList<CalendarDay> eventDates;

    public boolean isWeekly;

    private DateSingleton(){
        currentTime = System.currentTimeMillis();
        currentDate = new Date(currentTime);
        selectedCal = Calendar.getInstance();

        todayCal = Calendar.getInstance();

        selectedCal.setTime(currentDate);

        todayCal.setTime(currentDate);

        eventDates = new ArrayList<>();

    }

    public static DateSingleton getInstance()
    {
        return instance;
    }


    //다음달로 넘어갔는지 이전달로 갔는지에 대한 체크
    public void changeMonth(Calendar changeCal)
    {
        //다음달
        if(changeCal.getTime().getTime() > selectedCal.getTime().getTime())
        {
            selectedCal.add(Calendar.MONTH, +1);
        }
        //이전달
        else
        {
            selectedCal.add(Calendar.MONTH, -1);
        }

        //1일로 초기화
        selectedCal.add(Calendar.DATE, -(selectedCal.getTime().getDate()) + 1);
    }


    //다음주 혹은 저번주로 넘어갔는지에 대한 체크
    public void changeWeek(Calendar changeCal)
    {
        if(changeCal.getTime().getTime() > selectedCal.getTime().getTime())
        {
            selectedCal.add(Calendar.DATE, +7);
        }
        else
        {
            selectedCal.add(Calendar.DATE, -7);
        }
    }

    //어제
    public void previousDay()
    {
        selectedCal.add(Calendar.DATE, -1);
    }
    //오늘
    public void nextDay()
    {
        selectedCal.add(Calendar.DATE, +1);
    }
}
