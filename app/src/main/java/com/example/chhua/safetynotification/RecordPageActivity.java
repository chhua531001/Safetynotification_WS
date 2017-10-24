package com.example.chhua.safetynotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RecordPageActivity extends AppCompatActivity {

    String targetID = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_page);

        //隱藏ActionBar(App最上面的工具欄)
        android.support.v7.app.ActionBar m_myActionBar = getSupportActionBar();
        m_myActionBar.hide();
    }

    public void alarmClick(View v) {
        Log.println(Log.INFO, targetID, "alarm Button Click");

        Intent i = new Intent(this, AlarmPageActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);

    }

    public void transportClick(View v) {
        Log.println(Log.INFO, targetID, "transport Button Click");

        Intent i = new Intent(this, TransportPageActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);

    }

    public void recordClick(View v) {
        Log.println(Log.INFO, targetID, "record Button Click");

    }

    public void positionClick(View v) {
        Log.println(Log.INFO, targetID, "position Button Click");

        Intent i = new Intent(this, PositionPageActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(0, 0);

    }
}
