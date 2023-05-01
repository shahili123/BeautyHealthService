package com.test.beautyhealthservice.Fragments.BeautySpecialistFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.beautyhealthservice.ChatModule.Adapter.viewholder_user;
import com.test.beautyhealthservice.Helper;
import com.test.beautyhealthservice.R;
import com.test.beautyhealthservice.Users;

import java.util.ArrayList;


public class fragment_message extends Fragment
{
    private RecyclerView recyclerView;
    TextView txt_no_users;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    viewholder_user adapter_users;
    ArrayList<Users> list=new ArrayList<>();
    ArrayList<String> chats_list=new ArrayList<>();

    public fragment_message()
    {

    }

    public static fragment_message newInstance(String param1, String param2) {
        fragment_message fragment = new fragment_message();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);


        try {
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            txt_no_users=view.findViewById(R.id.txt_no_users);
            progressBar=view.findViewById(R.id.progress_circular);


            readUsers();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    private void readChats() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(Helper.GetData(getActivity(),"user_id"));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String chat_ids=snapshot.child("id").getValue(String.class);


                    try{
                        if(Helper.GetData(getActivity(),"user_id").equals(chat_ids)){

                        }
                        else{
                            chats_list.add(chat_ids);

                        }
                    }
                    catch (Exception e){

                    }


                }
                if(chats_list.size()>0){


                    readUsers();
                }
                else{
                    txt_no_users.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    try{
                        if (!user.getUser_id().equals(Helper.GetData(getActivity(),"user_id"))) {
                            list.add(user);
                        }
                    }
                    catch (Exception e){

                    }


                }
                if(list.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter_users = new viewholder_user(getContext(), list, true);
                    recyclerView.setAdapter(adapter_users);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    txt_no_users.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
