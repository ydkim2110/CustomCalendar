package com.comleoneo.customcalendarviewwithevents;

import android.content.Context;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

    private static final String TAG = CalendarAdapter.class.getSimpleName();
    
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
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "onBindViewHolder: displayDate: "+displayYear+", "+displayMonth+", "+displayDay);
        int currentYear = mCalendar.get(Calendar.YEAR);
        int currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        int currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "onBindViewHolder: currentDate: "+currentYear+", "+currentMonth+", "+currentDay);
        int todayYear = Calendar.getInstance().get(Calendar.YEAR);
        int todayMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (position % 7 == 0) {
            holder.tv_calendar_day.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        }
        else if (position % 7 == 6) {
            holder.tv_calendar_day.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
        }

        if (displayMonth == currentMonth && displayYear == currentYear) {

        }
        else {
            holder.tv_calendar_day.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }

        if (displayYear == todayYear && displayMonth == todayMonth && displayDay == todayDay) {
            holder.tv_calendar_day.setBackgroundResource(R.drawable.shape_oval);
            holder.tv_calendar_day.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }

        holder.tv_calendar_day.setText(""+dayNo);

        holder.setIRecyclerClickListener((view, i) -> {
            Toast.makeText(mContext, "Selected Day: "+mDateList.get(i), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConstraintLayout container;
        private TextView tv_calendar_day;

        private IRecyclerClickListener mIRecyclerClickListener;

        public void setIRecyclerClickListener(IRecyclerClickListener IRecyclerClickListener) {
            mIRecyclerClickListener = IRecyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            tv_calendar_day = itemView.findViewById(R.id.tv_calendar_day);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mIRecyclerClickListener.onClicked(v, getAdapterPosition());
        }
    }

}
