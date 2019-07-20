package com.comleoneo.customcalendarviewwithevents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecylcerAdapter extends RecyclerView.Adapter<EventRecylcerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Events> mEventsArrayList;

    public EventRecylcerAdapter(Context context, ArrayList<Events> eventsArrayList) {
        mContext = context;
        mEventsArrayList = eventsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_events_row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Events events = mEventsArrayList.get(position);
        holder.event.setText(events.getEVENT());
        holder.dateTxt.setText(events.getDATE());
        holder.time.setText(events.getTIME());
    }

    @Override
    public int getItemCount() {
        return mEventsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView dateTxt, event, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTxt = itemView.findViewById(R.id.event_date);
            event = itemView.findViewById(R.id.event_name);
            time = itemView.findViewById(R.id.event_time);
        }
    }
}
