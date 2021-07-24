package com.slowcoders.damds;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class SignupActivity extends AppCompatActivity {
    private EditText userName,userPhone,usersetPass,userConfirmPass;
    private Button signup,signupCancel;
    private DatabaseReference myRef,reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        usersetPass = findViewById(R.id.usersetPass);
        userConfirmPass = findViewById(R.id.userConfirmPass);
        signup = findViewById(R.id.signup);
        signupCancel = findViewById(R.id.signupCancel);

        Paper.init(this);

        myRef = FirebaseDatabase.getInstance().getReference("User");
        reference = FirebaseDatabase.getInstance().getReference("User");


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupUser();
            }
        });

        signupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    public void signupUser(){
        String Phone = userPhone.getText().toString();
        String Name = userName.getText().toString();
        String Password = usersetPass.getText().toString();
        String PassConfirm = userConfirmPass.getText().toString();

        if(Phone.matches("^[0-9]{11}$")){
        getuserslist(Name,Phone,Password,PassConfirm);
        }else{
            makeText(getApplicationContext(), "Please enter valid number!", LENGTH_LONG).show();
        }
    }

    public void getuserslist(String Name,String Phone,String Password,String PassConfirm) {

        reference.orderByChild("phone").equalTo(Phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    makeText(getApplicationContext(), "User with this phone already exists!", LENGTH_LONG).show();
                } else {
                    insertUser(Name,Phone,Password,PassConfirm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insertUser(String Name,String Phone,String Password,String PassConfirm){

            if(Password.equals(PassConfirm)){
                String empty = "";
                UserItem DataItem = new UserItem(Name,empty,empty, empty, Phone, Password, empty);

                myRef.child(Phone).setValue(DataItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        makeText(getApplicationContext(), "Your account created successfully!", LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeText(getApplicationContext(), "Error! Try again", LENGTH_LONG).show();
                    }
                });

            }else{
                Toast.makeText(this,"Both passwords should match!",Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SignupActivity.this.finish();
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
