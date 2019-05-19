package com.example.guk.scheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guk.scheduler.Adapter.ListViewCustomAdapter;
import com.example.guk.scheduler.DataManager.DataBaseOpenHelper;
import com.example.guk.scheduler.DataManager.DateSingleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/** 일일 뷰 프래그먼트 **/
public class DailyFragment extends Fragment {

    TextView dailyTv;
    CalenderActivity calenderActivity;
    FloatingActionButton DailyEditButton;

    ListView dailyListView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        dailyTv = (TextView)view.findViewById(R.id.dailyTv);

        ImageButton btn_PreviousDay = view.findViewById(R.id.previousDay_Btn);
        ImageButton btn_NextDay = view.findViewById(R.id.nextDay_Btn);

        //이전날 버튼
        btn_PreviousDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                SetPreviousDaily();
            }
        });
        //다음날 버튼
        btn_NextDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                SetNextDaily();
            }
        });

        //일정 추가 버튼
        DailyEditButton = (FloatingActionButton)view.findViewById(R.id.dailyEditButton);
        DailyEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);

                intent.putExtra("time", DateSingleton.getInstance().selectedCal.getTimeInMillis());

                startActivityForResult(intent, 3000);
            }
        });


        calenderActivity = (CalenderActivity)getActivity();

        dailyListView = (ListView)view.findViewById(R.id.DailyListView);
        dailyListView.setOnItemLongClickListener(listViewItemLongClick);

        //현재 포커스 된 날 스케줄 셋팅
        SetDailySchedule();

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

                    calenderActivity.fragmentAdapter.fragment_Monthly.eventCheckMonthly(DateSingleton.getInstance().selectedCal);
                    calenderActivity.refreshCalender();
                    break;
                }
            }
        }
        else if(resultCode == RESULT_CANCELED)
        {

        }
    }

    /** 리스트 뷰의 스케줄 노드 롱 클릭시 삭제 다이얼로그 호출 **/
    AdapterView.OnItemLongClickListener listViewItemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {

            final DataBaseOpenHelper helper;
            helper = new DataBaseOpenHelper(getActivity(), DataBaseOpenHelper.tableName, null, 1);
            final SQLiteDatabase database = helper.getWritableDatabase();
            final LinearLayout bgLayout = view.findViewById(R.id.daily_item_color);

            Calendar date = DateSingleton.getInstance().selectedCal;

            final int year = date.get(Calendar.YEAR);
            final int month = date.get(Calendar.MONTH);
            final int day = date.get(Calendar.DATE);

            final TextView titleTv = (TextView)view.findViewById(R.id.daily_item_title);

            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
            alert.setMessage("일정을 삭제 하시겠습니까? ");
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
                    //해당 스케줄 DB 삭제
                    helper.DeleteData(database, year, month, day, titleTv.getText().toString(), bgColor);
                    calenderActivity.refreshCalender();

                    //해당 날짜 마지막 이벤트 삭제시 달력의 이벤트 표시 지우기
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

    /** 당일 스케줄 셋팅 **/
    void SetDailySchedule()
    {
        int year =DateSingleton.getInstance().selectedCal.get(Calendar.YEAR);
        int month = DateSingleton.getInstance().selectedCal.get(Calendar.MONTH) + 1;
        int day =DateSingleton.getInstance().selectedCal.get(Calendar.DATE);

        String date = "";

        date += String.valueOf(day);
        switch (month)
        {
            case 1:
            {
                date += " January ";
                break;
            }
            case 2:
            {
                date += " February ";
                break;
            }
            case 3:
            {
                date += " March ";
                break;
            }
            case 4:
            {
                date += " April ";
                break;
            }
            case 5:
            {
                date += " May ";
                break;
            }
            case 6:
            {
                date += " June ";
                break;
            }
            case 7:
            {
                date += " July ";
                break;
            }
            case 8:
            {
                date += " August ";
                break;
            }
            case 9:
            {
                date += " September ";
                break;
            }
            case 10:
            {
                date += " October ";
                break;
            }
            case 11:
            {
                date += " November ";
                break;
            }
            case 12:
            {
                date += " December ";
                break;
            }
        }
        date += String.valueOf(year);
        dailyTv.setText(date);

        ScheduleLoad();
    }

    /** 일일 스케줄 리스트뷰에 스케줄 아이템 Load **/
    void ScheduleLoad()
    {
        Calendar date = Calendar.getInstance();
        date.set(DateSingleton.getInstance().selectedCal.get(Calendar.YEAR), DateSingleton.getInstance().selectedCal.get(Calendar.MONTH), DateSingleton.getInstance().selectedCal.get(Calendar.DATE));

        List<ScheduleInfo> scheduleList = new ArrayList<ScheduleInfo>();
        ListViewCustomAdapter customAdapter = new ListViewCustomAdapter(getActivity(), R.layout.listview_daily_item, scheduleList);
        dailyListView.setAdapter(customAdapter);


        DataBaseOpenHelper helper;
        helper = new DataBaseOpenHelper(getActivity(), DataBaseOpenHelper.tableName, null, 1);
        SQLiteDatabase database = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + DataBaseOpenHelper.tableName + " WHERE year = " +date.get(Calendar.YEAR) + " AND month = " + date.get(Calendar.MONTH) + " AND day = " + date.get(Calendar.DATE);
        Cursor cursor = database.rawQuery(sql, null);


        if(cursor.getCount() > 0)
        {
            while(cursor.moveToNext())
            {
                ScheduleInfo newSchedule = new ScheduleInfo();

                newSchedule.setSchedule(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
                scheduleList.add(newSchedule);
            }

            dailyListView.setAdapter(customAdapter);

        }

        cursor.close();

    }

    //이전 날
    void SetPreviousDaily()
    {
        DateSingleton.getInstance().previousDay();
        calenderActivity.refreshCalender();
    }
    //다음 날
    void SetNextDaily()
    {
        DateSingleton.getInstance().nextDay();
        calenderActivity.refreshCalender();
    }

    //새로고침
    public void refreshDaily()
    {
        SetDailySchedule();
    }
}
