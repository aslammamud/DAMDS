package com.slowcoders.damds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminTrackSerialList extends AppCompatActivity {
    private RecyclerView trackSerialRecylcerview;
    private AppointmentSerialListAdapter adapter;
    private List<AppointmentItem> items;
    private Button goBackAdminTSerialList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_track_serial_list);

        goBackAdminTSerialList = findViewById(R.id.goBackAdminTSerialList);

        trackSerialRecylcerview = findViewById(R.id.appointmentSerialRecylcerview);
        trackSerialRecylcerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        trackSerialRecylcerview.setLayoutManager(layoutManager);
        items = new ArrayList<AppointmentItem>();

        final String Date = getIntent().getStringExtra("DateTrack");
        final String Doctor = getIntent().getStringExtra("DoctTrack");

        Query query = FirebaseDatabase.getInstance().getReference("Appointments").child(Date)
        .orderByChild("doctorid").equalTo(Doctor);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AppointmentItem DataItem = snapshot.getValue(AppointmentItem.class);
                    items.add(DataItem);
                }
                adapter = new AppointmentSerialListAdapter(getApplicationContext(), items);
                trackSerialRecylcerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        goBackAdminTSerialList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
