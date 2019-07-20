package com.comleoneo.customcalendarviewwithevents;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyGridAdapter extends ArrayAdapter {

    private List<Date> mDateList;
    private Calendar mCurrentDate;
    private List<Events> mEventsList;
    private LayoutInflater mInflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dateList, Calendar currentDate, List<Events> eventsList) {
        super(context, R.layout.single_cell_layout);

        this.mDateList = dateList;
        this.mCurrentDate = currentDate;
        this.mEventsList = eventsList;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = mDateList.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = mCurrentDate.get(Calendar.MONTH) + 1;
        int currentYear = mCurrentDate.get(Calendar.YEAR);

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);

        }

        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.green));

        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView dayNumber = view.findViewById(R.id.calendar_day);
        TextView eventNumber = view.findViewById(R.id.events_id);
        dayNumber.setText(String.valueOf(dayNo));
        Calendar eventCalendar = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < mEventsList.size(); i++) {
            eventCalendar.setTime(convertStringToDate(mEventsList.get(i).getDATE()));

            if (dayNo == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)) {
                arrayList.add(mEventsList.get(i).getEVENT());
                eventNumber.setText(arrayList.size()+ " Events");
            }
        }

        return view;
    }

    private Date convertStringToDate(String eventDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        Date date = null;

        try {
            date = dateFormat.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public int getCount() {
        return mDateList.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return mDateList.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mDateList.get(position);
    }

}
