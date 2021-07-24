package com.slowcoders.damds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorsListPrivate extends AppCompatActivity {
    private Button goBackDoctorsList;
    private RecyclerView recyclerView;
    private DoctorsListAdapter adapter;
    private List<DoctorItem> items;
    private DatabaseReference reference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorslist);
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<DoctorItem>();

        String value = getIntent().getStringExtra("deptposition");
        //Toast.makeText(com.slowcoders.damds.DoctorsListGovt.this, value, Toast.LENGTH_SHORT).show();


        reference = FirebaseDatabase.getInstance().getReference("DepartmentsPrivate").child(value);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DoctorItem DataItem = snapshot.getValue(DoctorItem.class);
                    items.add(DataItem);
                }
                adapter = new DoctorsListAdapter(getApplicationContext(), items);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        goBackDoctorsList = findViewById(R.id.goBackDoctorsList);
        goBackDoctorsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
