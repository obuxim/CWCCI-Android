package com.octoriz.cwcci;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.octoriz.cwcci.adapter.NotificationAdapter;
import com.octoriz.cwcci.db.CWCCIDbHelper;
import com.octoriz.cwcci.db.DatabaseManager;
import com.octoriz.cwcci.features.NotificationCreateListener;
import com.octoriz.cwcci.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NotificationCreateListener {

    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    TextView mEmptyNotificationTextView;
    List<Notification> notificationList = new ArrayList<>();
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.notificationRecyclerView);
        mEmptyNotificationTextView = findViewById(R.id.empty_notification_view);

        databaseManager = new DatabaseManager(this);

        toggleEmptyNotifications();

        notificationAdapter = new NotificationAdapter(this, notificationList);

        notificationList.addAll(databaseManager.getAllNotifications());


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(notificationAdapter);


    }

    private void toggleEmptyNotifications() {
        if (databaseManager.getNotificationsCount() > 0) {
            mEmptyNotificationTextView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyNotificationTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_add_company) {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        }
        else if (id == R.id.nav_all_companies) {
            startActivity(new Intent(MainActivity.this, MemberActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, DetailActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onNotificationCreated(Notification notification) {
        notificationList.add(notification);
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        toggleEmptyNotifications();
        notificationList.clear();
        notificationList.addAll(databaseManager.getAllNotifications());
        notificationAdapter.notifyDataSetChanged();



    }
}
