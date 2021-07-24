package com.slowcoders.damds;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class AdminAppointments extends AppCompatActivity {
    DatePickerDialog DatePicker;
    private Button trackSerialButton, goBackTrackSerial;
    private EditText selectDateTrack,doctorIdTrack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_appointments);

        goBackTrackSerial = findViewById(R.id.goBackTrackSerial);
        goBackTrackSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        doctorIdTrack = findViewById(R.id.doctorIdTrack);
        trackSerialButton = findViewById(R.id.trackSerialButton);
        selectDateTrack = findViewById(R.id.selectDateTrack);
        selectDateTrack.setInputType(InputType.TYPE_NULL);
        selectDateTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePicker = new DatePickerDialog(AdminAppointments.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(Calendar.YEAR, year);
                                calendar1.set(Calendar.MONTH, monthOfYear);
                                calendar1.set(Calendar.DATE, dayOfMonth);

                                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                                selectDateTrack.setText(dateCharSequence);

                            }
                        }, year, month, day);
                DatePicker.show();
            }
        });

        trackSerialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String doctorIDtrack = doctorIdTrack.getText().toString();
                String datetrack = selectDateTrack.getText().toString();
                if(datetrack.equals("")){
                    makeText(getApplicationContext(), "Date can't be empty!", LENGTH_LONG).show();
                }else{
                    if(doctorIDtrack.equals("")){
                        makeText(getApplicationContext(), "Please enter doctor's id!", LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(AdminAppointments.this, AdminTrackSerialList.class);
                        intent.putExtra("DateTrack", datetrack);
                        intent.putExtra("DoctTrack", doctorIDtrack);
                        finish();
                        startActivity(intent);
                    }
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
