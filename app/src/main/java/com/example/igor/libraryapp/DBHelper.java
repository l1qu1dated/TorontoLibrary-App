package com.example.igor.libraryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Igor on 7/27/2017.
 */

public class DBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "libraryApp.db";
    public static final int DB_VERSION = 1;
    public static final String BRANCH_TABLE = "branch_table";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String POSTAL_CODE = "postalCode";
    public static final String TELEPHONE = "telephone";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";
    public static final String FAVORITE = "favorite";

    public static final String CREATE_TABLE_BRANCH = "CREATE TABLE " +
            BRANCH_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            NAME + " TEXT," + ADDRESS + " TEXT," + POSTAL_CODE + " TEXT," +
            TELEPHONE + " TEXT," + MONDAY + " TEXT," + TUESDAY + " TEXT," +
            WEDNESDAY + " TEXT," + THURSDAY + " TEXT," + FRIDAY + " TEXT," +
            SATURDAY + " TEXT," + SUNDAY + " TEXT," + FAVORITE + " INTEGER)";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);


        //context.deleteDatabase("library.db"); //to delete DATABASE

        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BRANCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + BRANCH_TABLE);

        onCreate(db);
    }

    public boolean insertData(String name, String address, String postal_code, String telephone_number,
                              String monday, String tuesday, String wednesday, String thursday,
                              String friday, String saturday, String sunday ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(ADDRESS, address);
        contentValues.put(POSTAL_CODE, postal_code);
        contentValues.put(TELEPHONE, telephone_number);
        contentValues.put(MONDAY, monday);
        contentValues.put(TUESDAY, tuesday);
        contentValues.put(WEDNESDAY, wednesday);
        contentValues.put(THURSDAY, thursday);
        contentValues.put(FRIDAY, friday);
        contentValues.put(SATURDAY, saturday);
        contentValues.put(SUNDAY, sunday);
        contentValues.put(FAVORITE, 0 );

        long result = db.insert(BRANCH_TABLE, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + BRANCH_TABLE, null);

        return res;
    }

}
