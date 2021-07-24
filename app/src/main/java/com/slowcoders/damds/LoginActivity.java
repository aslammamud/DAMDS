package com.slowcoders.damds;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private TextView gotoSignUp;
    private EditText loginPhone, loginPass;
    private Switch doctorSwitch;
    //private Switch rememberMeSwitch, adminSwitch;
    private String parentDbName = "user";
    private Button login, loginCancel;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gotoSignUp = findViewById(R.id.gotoSignUp);
        loginPhone = findViewById(R.id.loginPhone);
        loginPass = findViewById(R.id.loginPass);
        //rememberMeSwitch = findViewById(R.id.rememberMeSwitch);
        doctorSwitch = findViewById(R.id.doctorSwitch);
        login = findViewById(R.id.login);
        loadingBar = new ProgressDialog(this);
        loginCancel = findViewById(R.id.loginCancel);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
                startActivity(intent);*/
                LoginAllow();
            }
        });
        loginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPhone.setText(null);
                loginPass.setText(null);
            }
        });
        gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                finish();
                startActivity(intent);
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
                AllowRememberAccountLogin(Phone, ParentDB);
            }
        }
    }


    public void AllowRememberAccountLogin(final String Phone, final String ParentDB) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(ParentDB).child(Phone).exists()) {

                    if (ParentDB.equals("Admin")) {
                        loadingBar.setMessage("Logging in as Admin...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        finish();
                        startActivity(intent);
                    } else if (ParentDB.equals("User")) {
                        loadingBar.setMessage("Logging in as User...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                    } else if (ParentDB.equals("Doctor")) {
                        loadingBar.setMessage("Logging in as Doctor...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
                        finish();
                        startActivity(intent);
                    }

                } else {
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void LoginAllow() {
        final String UserPhone = loginPhone.getText().toString();
        final String UserPass = loginPass.getText().toString();

        if (TextUtils.isEmpty(UserPhone)) {
            Toast.makeText(LoginActivity.this, "Please enter valid phone no.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(UserPass)) {
            Toast.makeText(LoginActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccount(UserPhone, UserPass);
        }
    }
    public void AllowAccount(final String UserPhone, final String UserPass) {

        Paper.book().write(Prevalent.UserPhoneKey, UserPhone);
        Paper.book().write(Prevalent.UserPasswordKey, UserPass);

        if (doctorSwitch.isChecked()) {
            parentDbName = "Doctor";
            Paper.book().write(Prevalent.ParentDB, parentDbName);
        } else {
            parentDbName = "User";
            Paper.book().write(Prevalent.ParentDB, parentDbName);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(UserPhone).exists()) {
                    DoctorItem usersData = dataSnapshot.child(parentDbName).child(UserPhone).getValue(DoctorItem.class);
                    if (usersData.getPhone().equals(UserPhone) || usersData.getId().equals(UserPhone)) {
                        if (usersData.getPassword().equals(UserPass)) {
                            if (parentDbName.equals("Doctor")) {
                                Toast.makeText(LoginActivity.this, "Doctor logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
                                finish();
                                startActivity(intent);
                            } else if (parentDbName.equals("User")) {

                                if(usersData.getPhone().equals("01521310261")){
                                    Toast.makeText(LoginActivity.this, "Admin logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    intent.putExtra("UserPhone", UserPhone);
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("UserPhone", UserPhone);
                                    finish();
                                    startActivity(intent);
                                }
                            }

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + UserPhone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
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
