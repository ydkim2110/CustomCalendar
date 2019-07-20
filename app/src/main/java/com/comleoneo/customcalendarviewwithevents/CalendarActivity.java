package com.comleoneo.customcalendarviewwithevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = CalendarActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CalendarAdapter mAdapter;

    private static final int MAX_CALENDAR_DAYS = 35;
    private List<Date> mDateList = new ArrayList<>();
    private Calendar mCalendar = Calendar.getInstance(Locale.KOREA);

    private Toolbar mToolbar;

    private int height = 0;
    private int linearLayoutHeight = 0;
    private int statusBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initView();

        mRecyclerView = findViewById(R.id.rv_calendar);
        mLayoutManager = new GridLayoutManager(this, 7);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));

        setUpCalendar();

    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(Common.dateFormat.format(mCalendar.getTime()));

        setMeasuredDimensions();

        Display display = CalendarActivity.this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        height = size.y-mToolbar.getLayoutParams().height-51-63;

        height = height/5;


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        setMeasuredDimensions();
        super.onWindowFocusChanged(hasFocus);
    }

    private void setMeasuredDimensions() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayoutHeight = linearLayout.getMeasuredHeight();
        statusBarHeight = getStatusBarHeight();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setUpCalendar() {
        mDateList.clear();

        Calendar monthCalendar = (Calendar) mCalendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.d(TAG, "setUpCalendar: firstDayOfMonth: "+firstDayOfMonth);
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while (mDateList.size() < MAX_CALENDAR_DAYS) {
            mDateList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mAdapter = new CalendarAdapter(CalendarActivity.this, mDateList, mCalendar, height);
        mRecyclerView.setAdapter(mAdapter);
    }
}
