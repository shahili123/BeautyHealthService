package com.test.beautyhealthservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterBeautySpecialistActivity extends AppCompatActivity {


    TextInputLayout txt_fullname,txt_email,txt_address,txt_password;
    CheckBox checkBox_hairstyle,checkBox_hairspa,checkBox_colouring,checkBox_facial,checkBox_eyebrow,checkBox_massage;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 200;
    ImageView user_image;
    private StorageReference storeage_reference;
    private Uri path;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    protected LocationManager locationManager;
    private String latitude="",longitude="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_beauty_specialist);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storeage_reference = FirebaseStorage.getInstance().getReference();

        txt_fullname=findViewById(R.id.txt_fname);
        txt_email=findViewById(R.id.txt_email);
        txt_address=findViewById(R.id.txt_address);
        txt_password=findViewById(R.id.txt_password);
        checkBox_hairstyle=findViewById(R.id.checkbox_hairstyle);
        checkBox_hairspa=findViewById(R.id.checkbox_hairspa);
        checkBox_colouring=findViewById(R.id.checkbox_colouring);
        checkBox_facial=findViewById(R.id.checkbox_facial);
        checkBox_eyebrow=findViewById(R.id.checkbox_eyebrow);
        checkBox_massage=findViewById(R.id.checkbox_massage);
        user_image=findViewById(R.id.image_user);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        Button btn_signup=findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txt_fullname.getEditText().getText().toString().equals("")){
                    txt_fullname.setError("Please enter full name");
                }
                if(txt_email.getEditText().getText().toString().equals("")){
                    txt_email.setError("Please enter email");
                }
                else  if(txt_address.getEditText().getText().toString().equals("")){
                    txt_address.setError("Please enter address");
                }
                else  if(txt_password.getEditText().getText().toString().equals("")){
                    txt_password.setError("Please enter password");
                }

                else{

                    if(path==null){
                        Toast.makeText(RegisterBeautySpecialistActivity.this,"Opps image not loaded properly kindly reselect image again",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Helper.showLoader(RegisterBeautySpecialistActivity.this,"Please wait  . . .");
                        mAuth.createUserWithEmailAndPassword(txt_email.getEditText().getText().toString(),txt_password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    try{
                                        CreateProfile();
                                    }
                                    catch (Exception e){
                                        Toast.makeText(RegisterBeautySpecialistActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                                    }



                                } else {
                                    Toast.makeText(RegisterBeautySpecialistActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    Helper.stopLoader();
                                }

                            }
                        });
                    }
                }
            }
        });



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        if (ContextCompat.checkSelfPermission(RegisterBeautySpecialistActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {



            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitude=String.valueOf(location.getLatitude());
                    longitude=String.valueOf(location.getLongitude());
                    locationManager.removeUpdates(this);
                }
            });


        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                user_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void CreateProfile() {
        if (path != null) {

            StorageReference sRef = storeage_reference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(path));
            sRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            String uploadId = databaseReference.push().getKey();
                            Users upload = new Users(uploadId,txt_fullname.getEditText().getText().toString().trim(),txt_email.getEditText().getText().toString(),txt_password.getEditText().getText().toString(),txt_address.getEditText().getText().toString(),latitude,longitude, downloadUrl.toString(),"beauty_specialist","");
                            databaseReference.child(uploadId).setValue(upload);
                            Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_LONG).show();
                            Helper.stopLoader();
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Helper.stopLoader();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        } else {
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(RegisterBeautySpecialistActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterBeautySpecialistActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(RegisterBeautySpecialistActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(RegisterBeautySpecialistActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(RegisterBeautySpecialistActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(@NonNull Location location) {
                                    latitude=String.valueOf(location.getLatitude());
                                    longitude=String.valueOf(location.getLongitude());

                                    locationManager.removeUpdates(this);
                                }
                            });


                        }
                } else {
                    Toast.makeText(RegisterBeautySpecialistActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}