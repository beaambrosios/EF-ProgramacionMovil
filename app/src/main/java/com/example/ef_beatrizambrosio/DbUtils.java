package com.example.ef_beatrizambrosio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {
    private DbHelper dbHelper;

    public DbUtils(Context context) {
        dbHelper = new DbHelper(context);
    }
    public void insertHolidays(List<Holiday> holidays) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Holiday holiday : holidays) {
            ContentValues values = new ContentValues();
            values.put(DbHelper.COLUMN_DATE, holiday.getDate());
            values.put(DbHelper.COLUMN_LOCALNAME, holiday.getLocalName());

            db.insertWithOnConflict(DbHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }

        db.close();
    }

    public List<Holiday> getAllHolidays() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Holiday> holidays = new ArrayList<>();
        String[] projection = {DbHelper.COLUMN_DATE, DbHelper.COLUMN_LOCALNAME};
        Cursor cursor = db.query(DbHelper.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_DATE));
                String localname = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LOCALNAME));
                holidays.add(new Holiday(date, localname));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return holidays;
    }

}
