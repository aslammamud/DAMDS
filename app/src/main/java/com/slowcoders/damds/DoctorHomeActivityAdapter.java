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

public class DoctorHomeActivityAdapter extends RecyclerView.Adapter<DoctorHomeActivityAdapter.ViewHolder> {
    private final Context context;
    private final List<AppointmentItem> items;

    public DoctorHomeActivityAdapter(Context context, List<AppointmentItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public DoctorHomeActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_home_adapter, parent, false);
        return new DoctorHomeActivityAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHomeActivityAdapter.ViewHolder holder, int position) {
        final AppointmentItem item = items.get(position);

        holder.doctAptSerialNo.setText(String.valueOf(position + 1));
        holder.docAptSerialName.setText(item.name);
        holder.docAptSerialTime.setText(item.time);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView doctAptSerialNo, docAptSerialName, docAptSerialTime;

        public ViewHolder(View itemView) {
            super(itemView);
            doctAptSerialNo = itemView.findViewById(R.id.doctAptSerialNo);
            docAptSerialName = itemView.findViewById(R.id.docAptSerialName);
            docAptSerialTime = itemView.findViewById(R.id.docAptSerialTime);

        }
    }
}
