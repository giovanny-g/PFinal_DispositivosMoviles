
package com.example.giovanny.fuelapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table vehicles(id integer primary key, name text, tank integer)");
        db.execSQL("create table fuel_records(id integer primary key, vid integer, date text, " +
                   "odometro integer, totalprice integer, liters integer, literprice integer, " +
                   "notes text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("drop table if exists vehicles");
        db.execSQL("create table vehicles(id integer primary key, name text, tank integer)");
    }
}