package com.slowcoders.damds;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
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

public class DoctorHomeActivity extends AppCompatActivity {
    DatePickerDialog DatePicker;
    private EditText selectDocAptDate;
    private TextView noAptShowDoctor;
    private RecyclerView docAptSerialRecylcerview;
    private DoctorHomeActivityAdapter adapter;
    private List<AppointmentItem> items;
    private Button docAptTrackSerial,doctorLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        Paper.init(this);

        noAptShowDoctor = findViewById(R.id.noAptShowDoctor);
        docAptTrackSerial = findViewById(R.id.docAptTrackSerial);
        doctorLogout = findViewById(R.id.doctorLogout);
        selectDocAptDate = findViewById(R.id.selectDocAptDate);
        selectDocAptDate.setInputType(InputType.TYPE_NULL);
        selectDocAptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePicker = new DatePickerDialog(DoctorHomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(Calendar.YEAR, year);
                                calendar1.set(Calendar.MONTH, monthOfYear);
                                calendar1.set(Calendar.DATE, dayOfMonth);

                                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                                selectDocAptDate.setText(dateCharSequence);

                            }
                        }, year, month, day);
                DatePicker.show();
            }
        });

        docAptSerialRecylcerview = findViewById(R.id.docAptSerialRecylcerview);
        docAptSerialRecylcerview.setVisibility(View.GONE);
        noAptShowDoctor.setVisibility(View.VISIBLE);
        docAptSerialRecylcerview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        docAptSerialRecylcerview.setLayoutManager(layoutManager);
        items = new ArrayList<AppointmentItem>();


        docAptTrackSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date = selectDocAptDate.getText().toString();
                String Phone = Paper.book().read(Prevalent.UserPhoneKey);
                String Pass = Paper.book().read(Prevalent.UserPasswordKey);
                String ParentDB = Paper.book().read(Prevalent.ParentDB);

                noAptShowDoctor.setVisibility(View.GONE);
                docAptSerialRecylcerview.setVisibility(View.VISIBLE);

                if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
                    if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                        Query query = FirebaseDatabase.getInstance().getReference("Appointments").child(Date)
                                .orderByChild("doctorid").equalTo(Phone);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                items.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    AppointmentItem DataItem = snapshot.getValue(AppointmentItem.class);
                                    items.add(DataItem);
                                }
                                adapter = new DoctorHomeActivityAdapter(getApplicationContext(), items);
                                docAptSerialRecylcerview.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });



        doctorLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Paper.book().write(Prevalent.UserPhoneKey, "id");
                                Paper.book().write(Prevalent.UserPasswordKey, "pass");
                                Paper.book().write(Prevalent.ParentDB, "user");
                                DoctorHomeActivity.this.finish();
                                startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

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
