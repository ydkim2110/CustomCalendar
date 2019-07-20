package com.comleoneo.customcalendarviewwithevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CustomCalendarView mCustomCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomCalendarView = findViewById(R.id.custom_calendar_view);
    }
}
