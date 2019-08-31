package com.octoriz.cwcci.db;

import android.provider.BaseColumns;

public final class NotificationContract {
    private NotificationContract() {}

    public static class NotificationEntry implements BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_NAME_DATA = "data";
        public static final String COLUMN_NAME_MEMBER_NAME = "member_name";
        public static final String COLUMN_NAME_RECEIVED_AT = "received_at";
        public static final String COLUMN_NAME_SEEN_AT = "seen_at";
    }

    public static final String SQL_CREATE_NOTIFICATION_ENTRIES =
            "CREATE TABLE " + NotificationEntry.TABLE_NAME + " (" +
                    NotificationEntry._ID + " INTEGER PRIMARY KEY," +
                    NotificationEntry.COLUMN_NAME_DATA + " TEXT," +
                    NotificationEntry.COLUMN_NAME_MEMBER_NAME + " TEXT," +
                    NotificationEntry.COLUMN_NAME_RECEIVED_AT + " INTEGER," +
                    NotificationEntry.COLUMN_NAME_SEEN_AT + " INTEGER)";

    public static final String SQL_DELETE_NOTIFICATION_ENTRIES =
            "DROP TABLE IF EXISTS " + NotificationEntry.TABLE_NAME;
}
