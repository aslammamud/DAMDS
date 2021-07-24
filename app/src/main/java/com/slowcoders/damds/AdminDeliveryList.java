package com.slowcoders.damds;

import android.os.Bundle;
import android.text.TextUtils;
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

import io.paperdb.Paper;

public class AdminDeliveryList extends AppCompatActivity {
    private RecyclerView deliveryRecylcerview;
    private DeliveryListAdapter adapter;
    private List<MedicineOrderItem> items;
    private Button goBackDelivery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        Paper.init(this);

        goBackDelivery = findViewById(R.id.goBackDelivery);
        deliveryRecylcerview = findViewById(R.id.deliveryRecylcerview);
        deliveryRecylcerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        deliveryRecylcerview.setLayoutManager(layoutManager);
        items = new ArrayList<MedicineOrderItem>();




        goBackDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        if (Prevalent.ParentDB.isEmpty() || Prevalent.UserPhoneKey.isEmpty() || Prevalent.UserPasswordKey.isEmpty()) {
            Paper.book().write(Prevalent.UserPhoneKey, "id");
            Paper.book().write(Prevalent.UserPasswordKey, "pass");
            Paper.book().write(Prevalent.ParentDB, "user");
        }
        searchAcc();
    }
    public void searchAcc() {
        String Phone = Paper.book().read(Prevalent.UserPhoneKey);
        String Pass = Paper.book().read(Prevalent.UserPasswordKey);
        String ParentDB = Paper.book().read(Prevalent.ParentDB);

        if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
            if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                Query query = FirebaseDatabase.getInstance().getReference("Prescriptions").orderByChild("phone");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MedicineOrderItem DataItem = snapshot.getValue(MedicineOrderItem.class);
                            items.add(DataItem);
                        }
                        adapter = new DeliveryListAdapter(getApplicationContext(), items);
                        deliveryRecylcerview.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
