package com.example.guk.scheduler;

/** 리스트뷰 아이템 클래스 **/
public class ScheduleInfo {
    private String title, content;
    private Integer year, month, day, color;

    public void setSchedule(Integer year, Integer month, Integer day, String title, String content, Integer color)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public Integer getDay()
    {
        return this.day;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public Integer getColor(){return this.color;}

}