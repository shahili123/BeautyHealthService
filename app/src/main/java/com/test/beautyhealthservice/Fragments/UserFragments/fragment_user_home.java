package com.test.beautyhealthservice.Fragments.UserFragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.test.beautyhealthservice.BSpecialistModel;
import com.test.beautyhealthservice.Helper;
import com.test.beautyhealthservice.R;
import com.test.beautyhealthservice.viewholder_beauty_specialist;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_user_home extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    TextView txt_username;
    EditText txt_search;
    ImageView btn_hair_style,btn_hair_spa,btn_colouring,btn_facial,btn_eyebrows,btn_massage;

    CircleImageView image_user;
    DatabaseReference databaseReference;
    ArrayList<BSpecialistModel> list_beauty_specialist = new ArrayList<>();
    viewholder_beauty_specialist adapter;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public fragment_user_home() {
    }

    public static fragment_user_home newInstance(String param1, String param2) {
        fragment_user_home fragment = new fragment_user_home();
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
        View root = inflater.inflate(R.layout.fragment_user_home, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        txt_username=root.findViewById(R.id.txt_user_name);
        txt_search=root.findViewById(R.id.txt_search);
        image_user=root.findViewById(R.id.image_user);
        Picasso.get().load(Helper.GetData(getActivity(),"image_url")).into(image_user);
        txt_username.setText("Hello, "+Helper.GetData(getActivity(),"name"));
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        btn_hair_style=root.findViewById(R.id.btn_hair_style);
        btn_hair_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_hair_spa=root.findViewById(R.id.btn_hair_spa);
        btn_hair_spa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_colouring=root.findViewById(R.id.btn_colouring);
        btn_colouring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_facial=root.findViewById(R.id.btn_facial);
        btn_facial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_massage=root.findViewById(R.id.btn_massage);
        btn_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_eyebrows=root.findViewById(R.id.btn_eye_brow);
        btn_eyebrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        fetch_beauty_specialist();
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

    public void fetch_beauty_specialist(){
        Helper.showLoader(getActivity(),"Please wait we are fetching beauty specialist . . .");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // parsing all data
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                  String type=  postSnapshot.child("type").getValue(String.class);

                  ArrayList<String > list_services=new ArrayList<>();
                    for (DataSnapshot services_postSnapshot : postSnapshot.child("list_services").getChildren()) {

                        list_services.add(services_postSnapshot.getValue().toString());

                    }

                    if(type.equals("beauty_specialist")){
                        list_beauty_specialist.add(new BSpecialistModel(postSnapshot.child("id").getValue(String.class),postSnapshot.child("name").getValue(String.class),postSnapshot.child("email").getValue(String.class),postSnapshot.child("address").getValue(String.class),postSnapshot.child("latitude").getValue(String.class),postSnapshot.child("longitude").getValue(String.class),"",postSnapshot.child("image").getValue(String.class),list_services,postSnapshot.child("type").getValue(String.class)));

                  }

                }
                if(list_beauty_specialist.size()>0){
                    Helper.stopLoader();
                   adapter =new viewholder_beauty_specialist(getActivity(),list_beauty_specialist);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Helper.stopLoader();
                    Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


