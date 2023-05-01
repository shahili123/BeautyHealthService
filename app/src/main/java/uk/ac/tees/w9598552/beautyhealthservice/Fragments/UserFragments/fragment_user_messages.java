package uk.ac.tees.w9598552.beautyhealthservice.Fragments.UserFragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import uk.ac.tees.w9598552.beautyhealthservice.BSpecialistModel;
import uk.ac.tees.w9598552.beautyhealthservice.ChatModule.Adapter.viewholder_bspecialist_chats;
import uk.ac.tees.w9598552.beautyhealthservice.Helper;
import uk.ac.tees.w9598552.beautyhealthservice.R;


public class fragment_user_messages extends Fragment
{
    private RecyclerView recyclerView;
    TextView txt_no_users;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ProgressBar progressBar;
    viewholder_bspecialist_chats userAdapter;
    ArrayList<BSpecialistModel> list=new ArrayList<>();
    ArrayList<String> chats_list=new ArrayList<>();

    public fragment_user_messages()
    {

    }


    public static fragment_user_messages newInstance(String param1, String param2) {
        fragment_user_messages fragment = new fragment_user_messages();
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

        View view = inflater.inflate(R.layout.fragment_user_messages, container, false);


        try {
            recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            txt_no_users=view.findViewById(R.id.txt_no_users);
            progressBar=view.findViewById(R.id.progress_circular);


           readChats();
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
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String type=  postSnapshot.child("type").getValue(String.class);
                    String user_id=  postSnapshot.child("user_id").getValue(String.class);
                    try {
                            if (type.equals("beauty_specialist")) {
                                for(int i = 0; i < chats_list.size(); i++){
                                    if(chats_list.get(i).equals(user_id)){
                                        list.add(new BSpecialistModel(user_id, postSnapshot.child("name").getValue(String.class), postSnapshot.child("email").getValue(String.class), postSnapshot.child("address").getValue(String.class), postSnapshot.child("latitude").getValue(String.class), postSnapshot.child("longitude").getValue(String.class), "", postSnapshot.child("image").getValue(String.class), null, postSnapshot.child("type").getValue(String.class), postSnapshot.child("token").getValue(String.class)));
                                    }

                                }


                            }

                    }
                    catch (Exception e){

                    }


                }
                if(list.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    userAdapter = new viewholder_bspecialist_chats(getContext(), list, true);
                    recyclerView.setAdapter(userAdapter);
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
