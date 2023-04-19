package com.test.beautyhealthservice;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout txt_full_name,txt_email,txt_phone,txt_password;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 200;
    ImageView user_image;
    private StorageReference storeage_reference;
    private Uri path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storeage_reference = FirebaseStorage.getInstance().getReference();

        txt_full_name=findViewById(R.id.txt_fname);
        txt_email=findViewById(R.id.txt_email);
        txt_phone=findViewById(R.id.txt_ph);
        txt_password=findViewById(R.id.txt_password);
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
                if(txt_full_name.getEditText().getText().toString().equals("")){
                    txt_full_name.setError("Please enter full name");
                }
                if(txt_email.getEditText().getText().toString().equals("")){
                    txt_email.setError("Please enter email");
                }
                else  if(txt_phone.getEditText().getText().toString().equals("")){
                    txt_phone.setError("Please enter phone number");
                }
                else  if(txt_password.getEditText().getText().toString().equals("")){
                    txt_password.setError("Please enter password");
                }

                else{

                    if(path==null){
                        Toast.makeText(RegisterActivity.this,"Opps image not loaded properly kindly reselect image again",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Helper.showLoader(RegisterActivity.this,"Please wait  . . .");
                        mAuth.createUserWithEmailAndPassword(txt_email.getEditText().getText().toString(),txt_password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if (task.isSuccessful()) {
                                    try{
                                        CreateProfile();
                                    }
                                    catch (Exception e){
                                        Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                                    }



                                } else {
                                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    Helper.stopLoader();
                                }

                            }
                        });
                        CreateProfile();
                    }
                }
            }
        });


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
                                    Users upload = new Users(uploadId,txt_full_name.getEditText().getText().toString().trim(),txt_email.getEditText().getText().toString(),txt_password.getEditText().getText().toString(),"","", downloadUrl.toString());
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

}
