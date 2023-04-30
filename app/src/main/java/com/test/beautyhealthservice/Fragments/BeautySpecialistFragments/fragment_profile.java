package com.test.beautyhealthservice.Fragments.BeautySpecialistFragments;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.test.beautyhealthservice.Helper;
import com.test.beautyhealthservice.R;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_profile extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextInputLayout txt_full_name,txt_email,txt_address;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 200;
    CircleImageView user_image;
    private StorageReference storeage_reference;
    private Uri path;

    boolean flag_image=false;
    public fragment_profile() {
    }
    public static fragment_profile newInstance(String param1, String param2) {
        fragment_profile fragment = new fragment_profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storeage_reference = FirebaseStorage.getInstance().getReference();

        txt_full_name=root.findViewById(R.id.txt_fname);
        txt_email=root.findViewById(R.id.txt_email);
        txt_email.getEditText().setText(Helper.GetData(getActivity(),"email"));
        fetch_profile(txt_email.getEditText().getText().toString().trim());
        txt_address=root.findViewById(R.id.txt_address);
        user_image=root.findViewById(R.id.image_user);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        Button btn_signup=root.findViewById(R.id.btn_update);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_full_name.getEditText().getText().toString().equals("")){
                    txt_full_name.setError("Please enter full name");
                }
                if(txt_email.getEditText().getText().toString().equals("")){
                    txt_email.setError("Please enter email");
                }
                else  if(txt_address.getEditText().getText().toString().equals("")){
                    txt_address.setError("Please enter address");
                }
                else{

                    if(flag_image){
                        Helper.showLoader(getActivity(),"Please wait  . . .");

                        UpdateProfile();

                    }
                    else {


                        HashMap<String ,String> hashMap=new HashMap<>();
                        hashMap.put("name",txt_full_name.getEditText().getText().toString());
                        hashMap.put("address",txt_address.getEditText().getText().toString());
                        databaseReference.child(Helper.GetData(getActivity(),"user_id")).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Helper.stopLoader();
                                    flag_image=false;

                                    Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }
                }
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                try{
                    String deviceToken = task.getResult();
                    update_token(deviceToken);

                }
                catch (Exception e){

                }

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void update_token(String token){
        HashMap<String, Object> result = new HashMap<>();
        result.put("token", token);
        DatabaseReference reference_user = FirebaseDatabase.getInstance().getReference("Users");
        reference_user.child(Helper.GetData(getActivity(),"user_id")).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                user_image.setImageBitmap(bitmap);
                flag_image=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void UpdateProfile() {
        if (path != null) {

            StorageReference sRef = storeage_reference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(path));
            sRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            update_record(Helper.GetData(getActivity(),"user_id"),txt_full_name.getEditText().getText().toString(),txt_address.getEditText().getText().toString(),downloadUrl.toString());



                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Helper.stopLoader();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        } else {
        }
    }

    public void fetch_profile(String email){

        Helper.showLoader(getActivity(),"Please wait fetching profile information . . .");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    Helper.stopLoader();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {



                        txt_full_name.getEditText().setText(childSnapshot.child("name").getValue(String.class));
                        txt_address.getEditText().setText(childSnapshot.child("address").getValue(String.class));
                        Picasso.get().load(childSnapshot.child("image").getValue(String.class)).into(user_image);


                    }
                }
                else{
                    Toast.makeText(getActivity(),"Not found",Toast.LENGTH_LONG).show();
                    Helper.stopLoader();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Helper.stopLoader();
            }
        });
    }

    private void update_record(String user_id,String name,String address,String image){
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("address",address);
        hashMap.put("image",image);
        databaseReference.child(user_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Helper.stopLoader();
                    flag_image=false;
                    Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}

