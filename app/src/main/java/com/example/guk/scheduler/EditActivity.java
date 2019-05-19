package com.example.guk.scheduler;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.guk.scheduler.DataManager.DataBaseOpenHelper;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.util.Calendar;

/** 스케줄 등록 엑티비티 **/
public class EditActivity extends AppCompatActivity {

    DatePicker datePicker;
    EditText titleEdit;
    EditText contentEdit;

    Button cancelButton;
    Button saveButton;

    ColorPicker colorPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        datePicker = (DatePicker)findViewById(R.id.DatePicker);
        titleEdit = (EditText)findViewById(R.id.scheduleTitleEt);
        contentEdit = (EditText)findViewById(R.id.scheduleContentEt);

        cancelButton = (Button)findViewById(R.id.cancel_button);
        saveButton = (Button)findViewById(R.id.save_button);

        cancelButton.setOnClickListener(cancelButtonListener);
        saveButton.setOnClickListener(saveButtonListener);

        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        colorPicker.setOldCenterColor(Color.CYAN);


        Intent intent = getIntent();

        long time = intent.getExtras().getLong("time");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), null);

    }

    //일정 등록 캔슬
    Button.OnClickListener cancelButtonListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            setResult(RESULT_CANCELED, null);
            finish();
        }
    };

    //일정 등록
    Button.OnClickListener saveButtonListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent resultIntent = new Intent();
            resultIntent.putExtra("Year", datePicker.getYear());
            resultIntent.putExtra("Month", datePicker.getMonth());
            resultIntent.putExtra("Day", datePicker.getDayOfMonth());

            // 스케줄 DB 추가
            scheduleDBAdd(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), titleEdit.getText().toString(), contentEdit.getText().toString(), colorPicker.getColor());

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    //SQLite 스케줄 정보 삽입
    void scheduleDBAdd(int year, int month, int day, String title, String content, int color)
    {
        DataBaseOpenHelper helper;
        helper = new DataBaseOpenHelper(this, DataBaseOpenHelper.tableName, null, 1);
        SQLiteDatabase database = helper.getWritableDatabase();

        helper.InsertData(database, year, month, day, title, content, color);

    }

}
