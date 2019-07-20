package com.comleoneo.customcalendarviewwithevents;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    private ImageButton nextBtn;
    private ImageButton previousBtn;
    private TextView currentDate;
    private GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    private Calendar calendar = Calendar.getInstance(Locale.KOREA);
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.KOREA);
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.KOREA);
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    private SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

    private List<Date> dateList = new ArrayList<>();
    private List<Events> eventsList = new ArrayList<>();

    private AlertDialog mDialog;
    private DBOpenHelper mDBOpenHelper;

    private MyGridAdapter mAdapter;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeLayout();
        setUpCalendar();

        previousBtn.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            setUpCalendar();
        });

        nextBtn.setOnClickListener(v -> {
             calendar.add(Calendar.MONTH, 1);
             setUpCalendar();
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.add_newevent_layout, null);
                EditText eventName = addView.findViewById(R.id.event_name);
                TextView eventTime = addView.findViewById(R.id.event_time);
                ImageButton setTime = addView.findViewById(R.id.set_event_time);
                Button addEvent = addView.findViewById(R.id.add_event);

                setTime.setOnClickListener(v -> {
                    Calendar calendar = Calendar.getInstance();
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                            (TimePickerDialog.OnTimeSetListener) (view1, hourOfDay, minute) -> {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());

                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.KOREA);
                                String event_time = hformat.format(c.getTime());
                                eventTime.setText(event_time);

                    }, hours, minutes, false);
                    timePickerDialog.show();
                });

                String date = eventDateFormat.format(dateList.get(position));
                String month = monthFormat.format(dateList.get(position));
                String year = yearFormat.format(dateList.get(position));

                addEvent.setOnClickListener(v -> {
                    saveEvent(eventName.getText().toString(), eventTime.getText().toString(), date, month, year);
                    setUpCalendar();
                    mDialog.dismiss();
                });

                builder.setView(addView);
                mDialog = builder.create();
                mDialog.show();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dateList.get(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.show_events_layout, parent, false);

                RecyclerView recyclerView = showView.findViewById(R.id.rv_events);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecylcerAdapter adapter = new EventRecylcerAdapter(showView.getContext(), collectiEventByDate(date));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                builder.setView(showView);
                mDialog = builder.create();
                mDialog.show();

                return true;
            }
        });
    }

    private ArrayList<Events> collectiEventByDate(String _date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        mDBOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();
        Cursor cursor = mDBOpenHelper.readEvents(_date, db);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));
            Events events = new Events(event, time, date, month, year);
            arrayList.add(events);
        }

        cursor.close();
        mDBOpenHelper.close();

        return arrayList;
    }

    private void saveEvent(String event, String time, String date, String month, String year) {
        mDBOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();
        mDBOpenHelper.saveEvent(event, time, date, month, year, db);
        mDBOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void setUpCalendar() {
        String curtDate = dateFormat.format(calendar.getTime());
        currentDate.setText(curtDate);
        dateList.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        collectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dateList.size() < MAX_CALENDAR_DAYS) {
            dateList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        mAdapter = new MyGridAdapter(context, dateList, calendar, eventsList);
        gridView.setAdapter(mAdapter);
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void  initializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        nextBtn = view.findViewById(R.id.nextBtn);
        previousBtn = view.findViewById(R.id.previousBtn);
        currentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridview);
    }

    private void collectEventsPerMonth(String _month, String _year) {
        eventsList.clear();

        mDBOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();

        Cursor cursor = mDBOpenHelper.readEventsPerMonth(_month, _year, db);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));

            Events events = new Events(event, time, date, month, year);
            eventsList.add(events);
        }

        cursor.close();
        mDBOpenHelper.close();
    }

}
