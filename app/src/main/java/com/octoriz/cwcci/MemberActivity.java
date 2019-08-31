package com.octoriz.cwcci;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octoriz.cwcci.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends AppCompatActivity {

    List<String> memberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseManager databaseManager = new DatabaseManager(this);


        memberList.addAll(databaseManager.getAllMembers());

        ListView listView = (ListView) findViewById(R.id.member_list);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_member_list, R.id.textView, memberList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MemberActivity.this, SingleNotificationActivity.class);
                intent.putExtra("name", memberList.get(i));
                startActivity(intent);
                //Toast.makeText(MemberActivity.this, memberList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
