package com.example.guk.scheduler.DataManager;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/** SQLite 클래스 **/
public class DataBaseOpenHelper extends SQLiteOpenHelper {

    public static final String tableName = "Schedule";

    public DataBaseOpenHelper(Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /** 테이블 생성 */
    public void createTable(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE IF NOT EXISTS  " + tableName + " (year INTEGER,  month INTEGER, day INTEGER, title TEXT, content TEXT, color INTEGER);";
        try
        {

            db.execSQL(sql);
        }
        catch (SQLException e)
        {
        }

    }

    /** 행 추가 */
    public void InsertData(SQLiteDatabase db, int year, int month, int day, String title, String content, int color)
    {
        db.beginTransaction();
        try
        {

            String sql = String.format("INSERT INTO Schedule VALUES('%d', '%d', '%d', '%s', '%s', '%d');", year, month, day, title, content, color);

            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }


    /** 해당 행 삭제 */
    public void DeleteData(SQLiteDatabase db, int year, int month, int day, String title, int color)
    {
        db.beginTransaction();
        try
        {
            String sql = String.format("DELETE from Schedule WHERE year = '%d' AND month = '%d' AND day = '%d' AND title = '%s' AND color = '%d';", year, month, day, title, color);
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
    }



}
