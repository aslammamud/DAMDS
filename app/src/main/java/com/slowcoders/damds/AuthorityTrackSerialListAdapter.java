package com.slowcoders.damds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AuthorityTrackSerialListAdapter extends RecyclerView.Adapter<AuthorityTrackSerialListAdapter.ViewHolder> {
    private final Context context;
    private final List<AppointmentItem> items;

    public AuthorityTrackSerialListAdapter(Context context, List<AppointmentItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public AuthorityTrackSerialListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_track_serial_adapter, parent, false);
        return new AuthorityTrackSerialListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorityTrackSerialListAdapter.ViewHolder holder, int position) {
        final AppointmentItem item = items.get(position);

        holder.trackSerialNo.setText(String.valueOf(position + 1));
        holder.trackSerialDoctor.setText(item.doctorname);
        holder.trackSerialTime.setText(item.time);
        holder.trackSerialPatient.setText(item.name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView trackSerialNo, trackSerialDoctor, trackSerialTime, trackSerialPatient;

        public ViewHolder(View itemView) {
            super(itemView);
            trackSerialNo = itemView.findViewById(R.id.admintrackSerialNo);
            trackSerialDoctor = itemView.findViewById(R.id.admintrackSerialDoctor);
            trackSerialTime = itemView.findViewById(R.id.admintrackSerialTime);
            trackSerialPatient = itemView.findViewById(R.id.admintrackSerialPatient);

        }
    }
}
