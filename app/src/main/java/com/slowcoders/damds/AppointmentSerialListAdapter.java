package com.slowcoders.damds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class AppointmentSerialListAdapter extends RecyclerView.Adapter<AppointmentSerialListAdapter.ViewHolder> {
    private final Context context;
    private final List<AppointmentItem> items;

    public AppointmentSerialListAdapter(Context context, List<AppointmentItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public AppointmentSerialListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_appointment_serial_list_adapter, parent, false);
        return new AppointmentSerialListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentSerialListAdapter.ViewHolder holder, int position) {
        final AppointmentItem item = items.get(position);

        final int min = 1;
        final int max = 30;
        final int random = new Random().nextInt((max - min) + 1) + min;

        holder.trackSerialNo.setText(String.valueOf(position + 1));
        holder.trackSerialLive.setText(String.valueOf(random));
        holder.trackSerialTime.setText(item.time);
        holder.trackSerialDoctor.setText(item.doctorname);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trackSerialNo, trackSerialLive, trackSerialTime, trackSerialDoctor;

        public ViewHolder(View itemView) {
            super(itemView);
            trackSerialNo = itemView.findViewById(R.id.trackSerialNo);
            trackSerialLive = itemView.findViewById(R.id.trackSerialLive);
            trackSerialTime = itemView.findViewById(R.id.trackSerialTime);
            trackSerialDoctor = itemView.findViewById(R.id.trackSerialDoctor);

        }
    }
}
