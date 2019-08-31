package com.octoriz.cwcci;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.octoriz.cwcci.db.DatabaseManager;
import com.octoriz.cwcci.util.MyPreferences;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MyPreferences myPreferences = MyPreferences.getPreferences(this);

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.updateNotification();
//        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();

        final String token = myPreferences.getToken();

        final int count = databaseManager.getMembersCount();

        handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(token == null || count == 0)
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                else
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
