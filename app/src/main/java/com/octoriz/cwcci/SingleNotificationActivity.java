package com.octoriz.cwcci;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.octoriz.cwcci.adapter.NotificationAdapter;
import com.octoriz.cwcci.db.DatabaseManager;
import com.octoriz.cwcci.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class SingleNotificationActivity extends AppCompatActivity {

    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    TextView mEmptyNotificationTextView;
    List<Notification> notificationList = new ArrayList<>();
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_notification);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        String name = getIntent().getStringExtra("name");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(name);
        }

        recyclerView = findViewById(R.id.notificationRecyclerView);
        mEmptyNotificationTextView = findViewById(R.id.empty_notification_view);

        databaseManager = new DatabaseManager(this);

        toggleEmptyNotifications(name);

        notificationAdapter = new NotificationAdapter(this, notificationList);

        notificationList.addAll(databaseManager.getAllNotificationsByCompany(name));


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(notificationAdapter);

    }

    private void toggleEmptyNotifications(String name) {
        if (databaseManager.getNotificationsCountByCompany(name) > 0) {
            mEmptyNotificationTextView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyNotificationTextView.setVisibility(View.VISIBLE);
        }
    }
}
