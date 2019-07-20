package com.comleoneo.customcalendarviewwithevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE "
            + DBStructure.EVENT_TABLE_NAME
            + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBStructure.EVENT + " TEXT, "
            + DBStructure.TIME + " TEXT, "
            + DBStructure.DATE + " TEXT, "
            + DBStructure.MONTH + " TEXT, "
            + DBStructure.YEAR + " TEXT)";

    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS " + DBStructure.EVENT_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENTS_TABLE);
        onCreate(db);
    }

    public void saveEvent(String event, String time, String date, String month, String year, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DBStructure.EVENT, event);
        cv.put(DBStructure.TIME, time);
        cv.put(DBStructure.DATE, date);
        cv.put(DBStructure.MONTH, month);
        cv.put(DBStructure.YEAR, year);

        db.insert(DBStructure.EVENT_TABLE_NAME, null, cv);
    }

    public Cursor readEvents(String date, SQLiteDatabase db) {
        String[] projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String selection = DBStructure.DATE + " = ?";
        String[] selectionArgs = {date};

        return db.query(DBStructure.EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }

    public Cursor readEventsPerMonth(String month, String year, SQLiteDatabase db) {
        String[] projections = {DBStructure.EVENT, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR};
        String selection = DBStructure.MONTH + " = ? AND " + DBStructure.YEAR + " =?";
        String[] selectionArgs = {month, year};

        return db.query(DBStructure.EVENT_TABLE_NAME, projections, selection, selectionArgs, null, null, null);
    }

}
