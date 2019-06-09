package com.example.guk.scheduler.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guk.scheduler.R;
import com.example.guk.scheduler.ScheduleInfo;

import java.util.List;

/** 리스트뷰 커스텀 어댑터 */
public class ListViewCustomAdapter extends ArrayAdapter<ScheduleInfo> {

    private LayoutInflater mLayoutInflater;
    private int mResource;

    public ListViewCustomAdapter(Context context, int resource, List<ScheduleInfo> objects)
    {
        super(context, resource, objects);
        mResource = resource;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ScheduleInfo scheduleInfo = getItem(position);

        if(null == convertView)
        {
            convertView = mLayoutInflater.inflate(mResource, null);
            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 120));
        }

        //주간 리스트뷰 아이템
        if(mResource == R.layout.listview_weekly_item)
        {
            TextView titleTv = convertView.findViewById(R.id.weekly_item_title);
            titleTv.setText(scheduleInfo.getTitle());

            LinearLayout bgColor = convertView.findViewById(R.id.weekly_item_color);
            bgColor.setBackgroundColor(scheduleInfo.getColor());

        }
        //일일 리스트뷰 아이템
        else if(mResource == R.layout.listview_daily_item)
        {
            TextView titleTv = convertView.findViewById(R.id.daily_item_title);
            titleTv.setText(scheduleInfo.getTitle());

            TextView contentTv = convertView.findViewById(R.id.daily_item_content);
            contentTv.setText(scheduleInfo.getContent());

            LinearLayout bgColor = convertView.findViewById(R.id.daily_item_color);
            bgColor.setBackgroundColor(scheduleInfo.getColor());

            convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 240));
        }


        return convertView;
    }

}