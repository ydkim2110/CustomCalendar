package com.comleoneo.customcalendarviewwithevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView mCurrentDate;
    private ImageButton previousBtn;
    private ImageButton nextBtn;

    private int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initView();
        handleEvent();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            loadCalendar();
        }
    }

    public void loadCalendar() {
        Display display = CalendarActivity.this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int statusBarHeight = getStatusBarHeight();
        int toolbarHeight = findViewById(R.id.toolbar).getMeasuredHeight();
        int linearLayoutMonth = findViewById(R.id.linearLayout_month).getMeasuredHeight();
        int linearLayoutDay = findViewById(R.id.linearLayout).getMeasuredHeight();

        height = (size.y-statusBarHeight-toolbarHeight-linearLayoutMonth-linearLayoutDay)/5;

        mRecyclerView = findViewById(R.id.rv_calendar);
        mLayoutManager = new GridLayoutManager(this, 7);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));

        setUpCalendar();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mCurrentDate = findViewById(R.id.current_date);
        previousBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Calendar");
    }

    private void handleEvent() {
        previousBtn.setOnClickListener(v -> {
            mCalendar.add(Calendar.MONTH, -1);
            setUpCalendar();
        });
        nextBtn.setOnClickListener(v -> {
            mCalendar.add(Calendar.MONTH, 1);
            setUpCalendar();
        });
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
        mCurrentDate.setText(Common.dateFormat.format(mCalendar.getTime()));
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
