package com.octoriz.cwcci.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.octoriz.cwcci.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class CWCCIDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cwcci.db";

    private static CWCCIDbHelper cwcciDbHelper;
    private SQLiteDatabase sqLiteDatabase;
    Context context;


    public static synchronized CWCCIDbHelper getInstance(Context context){
        if(cwcciDbHelper==null){
            cwcciDbHelper = new CWCCIDbHelper(context);
        }
        return cwcciDbHelper;
    }

    public CWCCIDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MemberContract.SQL_CREATE_MEMBER_ENTRIES);
        sqLiteDatabase.execSQL(NotificationContract.SQL_CREATE_NOTIFICATION_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(MemberContract.SQL_DELETE_MEMBER_ENTRIES);
        sqLiteDatabase.execSQL(NotificationContract.SQL_DELETE_NOTIFICATION_ENTRIES);
        onCreate(sqLiteDatabase);
    }

}
