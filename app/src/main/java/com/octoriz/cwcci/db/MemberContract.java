package com.octoriz.cwcci.db;

import android.provider.BaseColumns;

public class MemberContract {
    private MemberContract() {}

    public static class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "members";
        public static final String COLUMN_NAME_MEMBER_NAME = "name";
        public static final String COLUMN_NAME_MEMBERSHIP_ID = "membership_id";
    }

    public static final String SQL_CREATE_MEMBER_ENTRIES =
            "CREATE TABLE " + MemberContract.MemberEntry.TABLE_NAME + " (" +
                    MemberContract.MemberEntry._ID + " INTEGER PRIMARY KEY," +
                    MemberContract.MemberEntry.COLUMN_NAME_MEMBER_NAME + " TEXT," +
                    MemberContract.MemberEntry.COLUMN_NAME_MEMBERSHIP_ID + " TEXT)";

    public static final String SQL_DELETE_MEMBER_ENTRIES =
            "DROP TABLE IF EXISTS " + NotificationContract.NotificationEntry.TABLE_NAME;
}
