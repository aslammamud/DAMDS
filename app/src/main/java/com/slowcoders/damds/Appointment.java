package com.slowcoders.damds;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import io.paperdb.Paper;

import static android.widget.Toast.makeText;


public class Appointment extends AppCompatActivity {

    DatePickerDialog DatePicker;
    private EditText selectDate, ageOfPatient, bloodGroupOfPaitent, symptompsOfPatient;
    private RadioGroup radioTimeSlot, genderOfPaitent;
    private RadioButton radioTimeButton, radioSexButton;
    private Button bookAppointment, cancelAppointment;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Paper.init(this);

        radioTimeSlot = findViewById(R.id.radioTimeSlot);
        selectDate = findViewById(R.id.selectDate);
        ageOfPatient = findViewById(R.id.ageOfPaitent);
        genderOfPaitent = findViewById(R.id.genderOfPaitent);
        bloodGroupOfPaitent = findViewById(R.id.bloodGroupOfPaitent);
        symptompsOfPatient = findViewById(R.id.symptompsOfPatient);

        final String DoctorName = getIntent().getStringExtra("DoctorName");
        final String DoctorID = getIntent().getStringExtra("DoctorID");

        bookAppointment = findViewById(R.id.bookAppointment);
        cancelAppointment = findViewById(R.id.cancelAppointment);




        selectDate.setInputType(InputType.TYPE_NULL);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePicker = new DatePickerDialog(Appointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(Calendar.YEAR, year);
                                calendar1.set(Calendar.MONTH, monthOfYear);
                                calendar1.set(Calendar.DATE, dayOfMonth);

                                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                                selectDate.setText(dateCharSequence);

                            }
                        }, year, month, day);
                DatePicker.show();
            }
        });


        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone = Paper.book().read(Prevalent.UserPhoneKey);
                String Pass = Paper.book().read(Prevalent.UserPasswordKey);
                String ParentDB = Paper.book().read(Prevalent.ParentDB);

                if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
                    if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {

                    reference = FirebaseDatabase.getInstance().getReference("User").child(Phone).child("name");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Integer timeSlotID = radioTimeSlot.getCheckedRadioButtonId();
                                    radioTimeButton = findViewById(timeSlotID);
                                    Integer genderID = genderOfPaitent.getCheckedRadioButtonId();
                                    radioSexButton = findViewById(genderID);

                                    String Name = snapshot.getValue().toString();
                                    String date = selectDate.getText().toString();
                                    String time = radioTimeButton.getText().toString();
                                    String age = ageOfPatient.getText().toString();
                                    String gender = radioSexButton.getText().toString();
                                    String bloodgroup = bloodGroupOfPaitent.getText().toString();
                                    String symptoms = symptompsOfPatient.getText().toString();

                                    Intent intent = new Intent(Appointment.this, AppointmentConfirmation.class);
                                    intent.putExtra("Date", date);
                                    intent.putExtra("Time", time);
                                    intent.putExtra("PatientName", Name);
                                    intent.putExtra("Age", age);
                                    intent.putExtra("Gender", gender);
                                    intent.putExtra("BloodGroup", bloodgroup);
                                    intent.putExtra("Symptoms", symptoms);
                                    intent.putExtra("Phone", Phone);
                                    intent.putExtra("DoctorName", DoctorName);
                                    intent.putExtra("DoctorID", DoctorID);
                                    finish();
                                    startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }
        });


        cancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
