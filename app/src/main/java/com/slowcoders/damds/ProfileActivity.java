package com.slowcoders.damds;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {
    private Button editUserProfile;
    private TextView userProfileName, userProfileAge, userProfileGender, userProfileBG, userProfilePhone, userProfileAddress,userProfilePassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Paper.init(this);

        editUserProfile = findViewById(R.id.editUserProfile);
        userProfileName = findViewById(R.id.userProfileName);
        userProfileAge = findViewById(R.id.userProfileAge);
        userProfileGender = findViewById(R.id.userProfileGender);
        userProfileBG = findViewById(R.id.userProfileBG);
        userProfilePhone = findViewById(R.id.userProfilePhone);
        userProfileAddress = findViewById(R.id.userProfileAddress);
        userProfilePassword = findViewById(R.id.userProfilePassword);

        editUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileUpdateActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navDashboardPage:
                        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navProfile:
                        return true;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setMessage("Are you sure you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Paper.book().write(Prevalent.UserPhoneKey, "id");
                                        Paper.book().write(Prevalent.UserPasswordKey, "pass");
                                        Paper.book().write(Prevalent.ParentDB, "user");
                                        finish();
                                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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

        searchAcc();
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
                        userProfileName.setText(snapshot.child("name").getValue().toString());
                        userProfileAge.setText(snapshot.child("age").getValue().toString());
                        userProfileBG.setText(snapshot.child("bloodgroup").getValue().toString());
                        userProfileGender.setText(snapshot.child("gender").getValue().toString());
                        userProfilePhone.setText(snapshot.child("phone").getValue().toString());
                        userProfilePassword.setText(snapshot.child("password").getValue().toString());
                        userProfileAddress.setText(snapshot.child("address").getValue().toString());
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
                            ProfileActivity.this.finish();
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
