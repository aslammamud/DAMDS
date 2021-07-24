package com.slowcoders.damds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.ViewHolder> {
    private final Context context;
    private final List<MedicineOrderItem> items;

    public DeliveryListAdapter(Context context, List<MedicineOrderItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public DeliveryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_delivery_list_adapter, parent, false);
        return new DeliveryListAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryListAdapter.ViewHolder holder, int position) {
        final MedicineOrderItem item = items.get(position);

        holder.deliverySerial.setText(String.valueOf(position + 1));
        holder.deliveryDate.setText(item.orderDate);
        holder.deliveryName.setText(item.customer);
        holder.deliveryStatus.setText(item.orderStatus);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deliverySerial, deliveryDate, deliveryName, deliveryStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            deliverySerial = itemView.findViewById(R.id.deliverySerial);
            deliveryDate = itemView.findViewById(R.id.deliveryDate);
            deliveryName = itemView.findViewById(R.id.deliveryName);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);

        }
    }
}
