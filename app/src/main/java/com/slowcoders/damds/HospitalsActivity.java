package com.slowcoders.damds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HospitalsActivity extends AppCompatActivity {
    private ImageButton govtHospitalButton, privateHospitalButton;
    private Button goBackHospitals;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);

        govtHospitalButton = findViewById(R.id.govtHospitalButton);
        privateHospitalButton = findViewById(R.id.privateHospitalButton);

        govtHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalsActivity.this, DepartmentsGovt.class);
                startActivity(intent);
            }
        });

        privateHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalsActivity.this, DepartmentsPrivate.class);
                startActivity(intent);
            }
        });

        goBackHospitals = findViewById(R.id.goBackHospitals);
        goBackHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}