package uk.ac.tees.w9598552.beautyhealthservice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button btn_login;
    private TextInputLayout txt_email, txt_password;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        checkLocationPermission();
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        if(Helper.GetData(LoginActivity.this,"user_id")!=null){

            if(Helper.GetData(LoginActivity.this,"type").equals("user")){
                finish();
                startActivity(new Intent(LoginActivity.this, UserHomePage.class));
            }
            else{
                finish();
                startActivity(new Intent(LoginActivity.this,BeautySpecialistHomePage.class));
            }
        }
        btn_login = (Button) findViewById(R.id.btn_signin);
        txt_email = (TextInputLayout) findViewById(R.id.txt_email);
        txt_password = (TextInputLayout) findViewById(R.id.txt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getEditText().getText().toString();
                String password = txt_password.getEditText().getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this,"Please enter email",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Helper.showLoader(LoginActivity.this,"Please wait . . .");
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                fetch_profile(txt_email.getEditText().getText().toString());

                            }
                            else {
                                Helper.stopLoader();
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        Button btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterModelSheet bottomSheet = new RegisterModelSheet();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "Permission Granted :)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void fetch_profile(String email){


        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    Helper.stopLoader();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        finish();
                        Helper.PutData(LoginActivity.this,"name",childSnapshot.child("full_name").getValue(String.class));
                        Helper.PutData(LoginActivity.this,"email",txt_email.getEditText().getText().toString());
                        Helper.PutData(LoginActivity.this,"user_id",childSnapshot.child("user_id").getValue(String.class));
                        Helper.PutData(LoginActivity.this,"type",childSnapshot.child("type").getValue(String.class));
                        Helper.PutData(LoginActivity.this,"image_url",childSnapshot.child("image_url").getValue(String.class));

                        String type=childSnapshot.child("type").getValue(String.class);
                        if(type.equals("user")){

                            startActivity(new Intent(LoginActivity.this,UserHomePage.class));
                        }
                        else{
                            startActivity(new Intent(LoginActivity.this,BeautySpecialistHomePage.class));

                        }

                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Not found",Toast.LENGTH_LONG).show();
                    Helper.stopLoader();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Helper.stopLoader();
            }
        });
    }
}
