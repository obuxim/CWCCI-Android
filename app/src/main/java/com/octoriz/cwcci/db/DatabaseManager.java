package com.octoriz.cwcci.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.octoriz.cwcci.model.Member;
import com.octoriz.cwcci.model.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DatabaseManager {

    private Context context;

    public DatabaseManager(Context context) {
        this.context = context;

    }

    public long insertNotification(Notification notification) {

        long id = -1;
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_DATA, notification.getData());
        contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME, notification.getName());
        contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_RECEIVED_AT, notification.getReceivedAt());
        contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_SEEN_AT, notification.getSeenAt());



        try {
            id = sqLiteDatabase.insertOrThrow(NotificationContract.NotificationEntry.TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Notification> getAllNotifications() {

        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(NotificationContract.NotificationEntry.TABLE_NAME, null, null, null, null, null, "_id"+" DESC", String.valueOf(50));

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Notification> notificationList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(NotificationContract.NotificationEntry._ID));
                        String data = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_DATA));
                        String name = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME));
                        String receivedAt = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_RECEIVED_AT));
                        String seenAt = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_SEEN_AT));
                        notificationList.add(new Notification(id, data, name, receivedAt, seenAt));
                    } while (cursor.moveToNext());

                    return notificationList;
                }
        } catch (Exception e) {

            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public List<Notification> getAllNotificationsByCompany(String memberName) {

        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(NotificationContract.NotificationEntry.TABLE_NAME, null, NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME + " = ? ", new String []{ memberName }, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToLast()) {
                    List<Notification> notificationList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(NotificationContract.NotificationEntry._ID));
                        String data = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_DATA));
                        String name = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME));
                        String receivedAt = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_RECEIVED_AT));
                        String seenAt = cursor.getString(cursor.getColumnIndex(NotificationContract.NotificationEntry.COLUMN_NAME_SEEN_AT));
                        notificationList.add(new Notification(id, data, name, receivedAt, seenAt));
                    } while (cursor.moveToPrevious());

                    return notificationList;
                }
        } catch (Exception e) {

            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public int getNotificationsCount() {
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + NotificationContract.NotificationEntry.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getMembersCount() {
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + MemberContract.MemberEntry.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNotificationsCountByCompany(String name) {
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getWritableDatabase();
        //String countQuery = "SELECT  * FROM " + NotificationContract.NotificationEntry.TABLE_NAME ;
        Cursor cursor = sqLiteDatabase.query(NotificationContract.NotificationEntry.TABLE_NAME, null,NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME + " = ? ", new String []{ name },null,null,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public long updateNotification()
    {
        long rowCount = 0;
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("dd.MM.yyyy 'at' hh:mm a");

        String date = ft.format(dNow);

        contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_SEEN_AT, date);
        Log.d("Splash Activity",date);

        try {
            rowCount = sqLiteDatabase.update(NotificationContract.NotificationEntry.TABLE_NAME, contentValues,
                    NotificationContract.NotificationEntry.COLUMN_NAME_SEEN_AT + " = ? ",
                    new String []{ "na" });
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List getAllMembers()
    {
        CWCCIDbHelper cwcciDbHelper = CWCCIDbHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = cwcciDbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(MemberContract.MemberEntry.TABLE_NAME, null, null, null, null, null, null, null);

            /**
             // If you want to execute raw query then uncomment below 2 lines. And comment out above line.
             String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_STUDENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<String> memberList = new ArrayList<>();
                    do {
                        String name = cursor.getString(cursor.getColumnIndex(MemberContract.MemberEntry.COLUMN_NAME_MEMBER_NAME));
                        memberList.add(name);
                    } while (cursor.moveToNext());

                    return memberList;
                }
        } catch (Exception e) {

            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }



//
//    public Student getStudentByRegNum(long registrationNum) {
//
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
//
//        Cursor cursor = null;
//        Student student = null;
//        try {
//
//            cursor = sqLiteDatabase.query(Config.TABLE_STUDENT, null,
//                    Config.COLUMN_STUDENT_REGISTRATION + " = ? ", new String[]{String.valueOf(registrationNum)},
//                    null, null, null);
//
//            /**
//             // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.
//             String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION, String.valueOf(registrationNum));
//             cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
//             */
//
//            if (cursor.moveToFirst()) {
//                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_STUDENT_ID));
//                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
//                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_STUDENT_REGISTRATION));
//                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_PHONE));
//                String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_EMAIL));
//
//                student = new Student(id, name, registrationNumber, phone, email);
//            }
//        } catch (Exception e) {
//            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
//        } finally {
//            if (cursor != null)
//                cursor.close();
//            sqLiteDatabase.close();
//        }
//
//        return student;
//    }
//
//    public long updateStudentInfo(Student student) {
//
//        long rowCount = 0;
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_STUDENT_NAME, student.getName());
//        contentValues.put(Config.COLUMN_STUDENT_REGISTRATION, student.getRegistrationNumber());
//        contentValues.put(Config.COLUMN_STUDENT_PHONE, student.getPhoneNumber());
//        contentValues.put(Config.COLUMN_STUDENT_EMAIL, student.getEmail());
//
//        try {
//            rowCount = sqLiteDatabase.update(Config.TABLE_STUDENT, contentValues,
//                    Config.COLUMN_STUDENT_ID + " = ? ",
//                    new String[]{String.valueOf(student.getId())});
//        } catch (SQLiteException e) {
//            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return rowCount;
//    }
//
//    public long deleteStudentByRegNum(long registrationNum) {
//        long deletedRowCount = -1;
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        try {
//            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_STUDENT,
//                    Config.COLUMN_STUDENT_REGISTRATION + " = ? ",
//                    new String[]{String.valueOf(registrationNum)});
//        } catch (SQLiteException e) {
//            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return deletedRowCount;
//    }
//
//    public boolean deleteAllStudents() {
//        boolean deleteStatus = false;
//        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//
//        try {
//            //for "1" delete() method returns number of deleted rows
//            //if you don't want row count just use delete(TABLE_NAME, null, null)
//            sqLiteDatabase.delete(Config.TABLE_STUDENT, null, null);
//
//            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_STUDENT);
//
//            if (count == 0)
//                deleteStatus = true;
//
//        } catch (SQLiteException e) {
//            Logger.d("Exception: " + e.getMessage());
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//        } finally {
//            sqLiteDatabase.close();
//        }
//
//        return deleteStatus;
//    }

}

