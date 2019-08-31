package com.octoriz.cwcci.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.octoriz.cwcci.MainActivity;
import com.octoriz.cwcci.LoginActivity;
import com.octoriz.cwcci.MainActivity;
import com.octoriz.cwcci.R;
import com.octoriz.cwcci.SplashActivity;
import com.octoriz.cwcci.db.CWCCIDbHelper;
import com.octoriz.cwcci.db.DatabaseManager;
import com.octoriz.cwcci.db.MemberContract;
import com.octoriz.cwcci.db.NotificationContract;
import com.octoriz.cwcci.model.Notification;
import com.octoriz.cwcci.singleton.MySingleton;
import com.octoriz.cwcci.util.MyPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String fcmUrl = "https://cwcci-laravel-lazyking.c9users.io/api/send";

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;

    CWCCIDbHelper cwcciDbHelper;
    SQLiteDatabase db;
    ContentValues contentValues;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        createNotificationChannel();

        cwcciDbHelper = CWCCIDbHelper.getInstance(this);
        db = cwcciDbHelper.getWritableDatabase();
        contentValues = new ContentValues();

        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("dd.MM.yyyy 'at' hh:mm a");

        Log.d("FCM" , remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String,String> map = remoteMessage.getData();

            String data = map.get("data");
            String name = map.get("name");
            String received = ft.format(dNow);
            String seen = "na";

            Notification notification = new Notification(-1, data, name, received, seen);

            DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());

            databaseManager.insertNotification(notification);


//            contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_DATA, map.get("data"));
//            contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_MEMBER_NAME, map.get("name"));
//            contentValues.put(NotificationContract.NotificationEntry.COLUMN_NAME_RECEIVED_AT, ft.format(dNow));
//
//
//            long newRowId = db.insert(NotificationContract.NotificationEntry.TABLE_NAME, null, contentValues);


            Log.d("FCM", "Message " + ft.format(dNow));
            Log.d("FCM", "Message data payload: " + map.get("data"));
            Log.d("FCM", "Message data payload: " + map.get("name"));
            getNotificationBuilder("New Message from CWCCI");
            sendNotification("New Message from CWCCI");
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "Exception: " + e.getMessage());
//            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification("cwcci",remoteMessage.getNotification().getBody());
            getNotificationBuilder(remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        MyPreferences.getPreferences(getApplicationContext()).setFirebaseToken(token);

        //sendRegistrationToServer(token);


    }
    // [END on_new_token]


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

//        try {
//            JSONObject data = json.getJSONObject("data");
//
//            String title = getString(R.string.message_from_octoplus_tv);
//            String message = data.getString("title");
//            String imageUrl = data.getString("image");
//
//            Log.d(TAG, "PUSH title: " + title);
//            Log.d(TAG, "PUSH message: " + message);
//            Log.d(TAG, "PUSH imageUrl: " + imageUrl);
//
//            sendNotification(title, message);
//
//        } catch (
//                JSONException e)
//
//        {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e)
//
//        {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
    }


    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "CWWCI Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from CWCCI");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(String body) {

        Intent notificationIntent = new Intent(this, SplashActivity.class);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent,  PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("CWCCI")
                .setContentText(body)
                .setSmallIcon(R.drawable.cwcci)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return mNotifyBuilder;
    }

    public void sendNotification(String body) {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder( body);
        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

}
