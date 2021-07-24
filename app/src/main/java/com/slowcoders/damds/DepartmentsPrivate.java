package com.slowcoders.damds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class DepartmentsPrivate extends AppCompatActivity {
    private Button goBackDepartments;
    private ListView listView;
    private DepartmentsAdapter adapter;
    private String[] department_names_array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        goBackDepartments = findViewById(R.id.goBackDepartments);
        listView = findViewById(R.id.listViewDept);

        department_names_array = getResources().getStringArray(R.array.private_hostpital_departments_array);

        adapter = new DepartmentsAdapter(this, department_names_array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = adapter.department_names_array[position];
                Intent intent = new Intent(DepartmentsPrivate.this, DoctorsListPrivate.class);
                intent.putExtra("deptposition", value);
                startActivity(intent);
            }
        });

        goBackDepartments.setOnClickListener(new View.OnClickListener() {
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
