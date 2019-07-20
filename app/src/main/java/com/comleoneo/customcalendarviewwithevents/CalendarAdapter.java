package com.comleoneo.customcalendarviewwithevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private Context mContext;
    private List<Date> mDateList;
    private Calendar mCalendar;
    private int mHeight;

    public CalendarAdapter(Context context, List<Date> dateList, Calendar calendar, int height) {
        mContext = context;
        mDateList = dateList;
        mCalendar = calendar;
        mHeight = height;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_calendar_day, parent, false);

        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        params.height = mHeight;
        view.setLayoutParams(params);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Date monthDate = mDateList.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        int currentYear = mCalendar.get(Calendar.YEAR);

        if (displayMonth == currentMonth && displayYear == currentYear) {

        }
        else {
            holder.tv_calendar_day.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }

        holder.tv_calendar_day.setText(""+dayNo);

    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_calendar_day;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_calendar_day = itemView.findViewById(R.id.tv_calendar_day);
        }
    }

}
