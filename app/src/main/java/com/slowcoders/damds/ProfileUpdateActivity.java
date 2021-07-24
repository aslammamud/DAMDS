package com.slowcoders.damds;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class ProfileUpdateActivity extends AppCompatActivity {
    private Button saveUserProfile,discardProfileEdit;
    private EditText editProfileName, editProfileAge, editProfileBG, editProfilePassword,editProfileAddress;
    private TextView editProfilePhone;
    private RadioGroup radio;
    private RadioButton radioButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        Paper.init(this);

        saveUserProfile = findViewById(R.id.saveUserProfile);
        discardProfileEdit = findViewById(R.id.discardProfileEdit);

        radio = findViewById(R.id.radio);

        editProfileName = findViewById(R.id.editProfileName);
        editProfileAge = findViewById(R.id.editProfileAge);
        editProfileBG = findViewById(R.id.editProfileBG);
        editProfilePhone = findViewById(R.id.editProfilePhone);
        editProfilePassword = findViewById(R.id.editProfilePassword);
        editProfileAddress = findViewById(R.id.editProfileAddress);
        searchAcc();
        saveUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Phone = Paper.book().read(Prevalent.UserPhoneKey);
                String Pass = Paper.book().read(Prevalent.UserPasswordKey);
                String ParentDB = Paper.book().read(Prevalent.ParentDB);

                if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
                    if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                        final DatabaseReference myRef;
                        myRef = FirebaseDatabase.getInstance().getReference().child("User").child(Phone);
                        int radioId = radio.getCheckedRadioButtonId();
                        radioButton = findViewById(radioId);

                        HashMap<String, Object> updatedValues = new HashMap<>();

                        updatedValues.put("name", editProfileName.getText().toString());
                        updatedValues.put("age", editProfileAge.getText().toString());
                        updatedValues.put("gender", radioButton.getText().toString());
                        updatedValues.put("bloodgroup", editProfileBG.getText().toString());
                        updatedValues.put("phone", editProfilePhone.getText().toString());
                        updatedValues.put("address", editProfileAddress.getText().toString());

                        myRef.updateChildren(updatedValues);
                        Toast.makeText(ProfileUpdateActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileUpdateActivity.this, ProfileActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navDashboardPage:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navProfile:
                        return true;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdateActivity.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Paper.book().write(Prevalent.UserPhoneKey, "id");
                                        Paper.book().write(Prevalent.UserPasswordKey, "pass");
                                        Paper.book().write(Prevalent.ParentDB, "user");
                                        finish();
                                        startActivity(new Intent(ProfileUpdateActivity.this, LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                }
                return false;
            }
        });

        if (Prevalent.ParentDB.isEmpty() || Prevalent.UserPhoneKey.isEmpty() || Prevalent.UserPasswordKey.isEmpty()) {
            Paper.book().write(Prevalent.UserPhoneKey, "id");
            Paper.book().write(Prevalent.UserPasswordKey, "pass");
            Paper.book().write(Prevalent.ParentDB, "user");
        }

        discardProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void searchAcc() {
        String Phone = Paper.book().read(Prevalent.UserPhoneKey);
        String Pass = Paper.book().read(Prevalent.UserPasswordKey);
        String ParentDB = Paper.book().read(Prevalent.ParentDB);

        if (Phone != "id" && Pass != "pass" && ParentDB != "user") {
            if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("User").child(Phone);
                RootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        editProfileName.setText(snapshot.child("name").getValue().toString());
                        editProfileAge.setText(snapshot.child("age").getValue().toString());
                        editProfileBG.setText(snapshot.child("bloodgroup").getValue().toString());
                        editProfilePhone.setText(snapshot.child("phone").getValue().toString());
                        editProfileAddress.setText(snapshot.child("address").getValue().toString());
                        editProfilePassword.setText(snapshot.child("password").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }



    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ProfileUpdateActivity.this.finish();
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
}
