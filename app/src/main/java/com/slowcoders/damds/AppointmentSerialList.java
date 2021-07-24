package com.slowcoders.damds;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;

public class AppointmentSerialList extends AppCompatActivity {
    DatePickerDialog DatePicker;
    private EditText selectAptDate;
    private TextView noAptShow;
    private RecyclerView trackSerialRecylcerview;
    private AppointmentSerialListAdapter adapter;
    private List<AppointmentItem> items;
    private Button appointmentTrackSerial,goBackTSerialList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_serial_list);

        Paper.init(this);
        noAptShow = findViewById(R.id.noAptShow);
        appointmentTrackSerial = findViewById(R.id.appointmentTrackSerial);
        goBackTSerialList = findViewById(R.id.goBackTSerialList);
        selectAptDate = findViewById(R.id.selectAptDate);
        selectAptDate.setInputType(InputType.TYPE_NULL);
        selectAptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePicker = new DatePickerDialog(AppointmentSerialList.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(Calendar.YEAR, year);
                                calendar1.set(Calendar.MONTH, monthOfYear);
                                calendar1.set(Calendar.DATE, dayOfMonth);

                                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                                selectAptDate.setText(dateCharSequence);

                            }
                        }, year, month, day);
                DatePicker.show();
            }
        });

        trackSerialRecylcerview = findViewById(R.id.appointmentSerialRecylcerview);
        trackSerialRecylcerview.setVisibility(View.GONE);
        noAptShow.setVisibility(View.VISIBLE);
        trackSerialRecylcerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        trackSerialRecylcerview.setLayoutManager(layoutManager);
        items = new ArrayList<AppointmentItem>();


        appointmentTrackSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date = selectAptDate.getText().toString();
                String Phone = Paper.book().read(Prevalent.UserPhoneKey);
                String Pass = Paper.book().read(Prevalent.UserPasswordKey);
                String ParentDB = Paper.book().read(Prevalent.ParentDB);

                noAptShow.setVisibility(View.GONE);
                trackSerialRecylcerview.setVisibility(View.VISIBLE);


                if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
                    if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                        Query query = FirebaseDatabase.getInstance().getReference("Appointments").child(Date)
                                .orderByChild("phone").equalTo(Phone);
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
                    }
                }
            }
        });



        goBackTSerialList.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
