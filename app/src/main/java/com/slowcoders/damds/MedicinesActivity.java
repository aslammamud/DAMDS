package com.slowcoders.damds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class MedicinesActivity extends AppCompatActivity {
    long maxid = 0;
    private Button uploadPrescription,uploadData,discardUPload, goBackMedicines;
    private EditText orderDescription, customerName, customerAddress;
    private ImageView presView;
    public Uri imageUri;

    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicines);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadPrescription = findViewById(R.id.uploadPrescription);
        uploadData = findViewById(R.id.uploadData);
        discardUPload = findViewById(R.id.discardUPload);
        orderDescription = findViewById(R.id.orderDescription);
        customerName = findViewById(R.id.customerName);
        customerAddress = findViewById(R.id.customerAddress);
        goBackMedicines = findViewById(R.id.goBackMedicines);

        presView = findViewById(R.id.presView);
        Paper.init(this);

        getCount();

        uploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        uploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPicture();
            }
        });

        discardUPload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goBackMedicines.setOnClickListener(new View.OnClickListener() {
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

        private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null){
            imageUri = data.getData();
            presView.setVisibility(View.VISIBLE);
            presView.setImageURI(imageUri);
        }
    }

    private void uploadPicture() {

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


        if (!Phone.equals("id") && !Pass.equals("pass") && !ParentDB.equals("user")) {
            if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
               /* getUserInfo(Phone, ParentDB,new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Log.d("TAG", value);
                    }
                });*/

                final ProgressDialog pd = new ProgressDialog(MedicinesActivity.this);
                pd.setTitle("Uploading Prescription...");
                pd.show();
                final String randomKey = UUID.randomUUID().toString();
                StorageReference mountainImagesRef = storageReference.child("images/" + randomKey);

                String prescriptionImage = randomKey;
                String CustomerName = customerName.getText().toString();
                String orderAddress = customerAddress.getText().toString();
                String orderdescription = orderDescription.getText().toString();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String status = "On Delivery";
                final DatabaseReference RootRef;
                myRef = FirebaseDatabase.getInstance().getReference("Prescriptions");
                MedicineOrderItem DataItem = new MedicineOrderItem(CustomerName,Phone,orderAddress,randomKey,orderdescription, currentDate, currentTime, status);

                myRef.child(String.valueOf(maxid + 1)).setValue(DataItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        makeText(getApplicationContext(), "Your order placed successfully!", LENGTH_LONG).show();
                        mountainImagesRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pd.dismiss();
                                Snackbar.make(findViewById(android.R.id.content),"Prescription Uploaded Successfully!", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed To Upload Prescription", Toast.LENGTH_LONG).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                pd.setMessage("Progress: " + (int) progressPercent + "%");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeText(getApplicationContext(), "Error! Try again", LENGTH_LONG).show();
                    }
                });

            }
        }
    }

/*    public interface MyCallback {
        void onCallback(String value);
    }

    public void getUserInfo(final String Phone, final String ParentDB, MyCallback myCallback) {

        final DatabaseReference userRef;
        userRef = FirebaseDatabase.getInstance().getReference(ParentDB).child(Phone);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String CustomerName = snapshot.child("name").getValue().toString();
                myCallback.onCallback(CustomerName);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }*/

    public void getCount() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference("Prescriptions");
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                maxid = (snapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
