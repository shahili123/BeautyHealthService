package uk.ac.tees.w9598552.beautyhealthservice.Fragments.BeautySpecialistFragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

import uk.ac.tees.w9598552.beautyhealthservice.Helper;
import uk.ac.tees.w9598552.beautyhealthservice.R;

public class fragment_setting extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    DatabaseReference databaseReference;
    ArrayList<String> list_services = new ArrayList<>();
    CheckBox checkBox_hairstyle, checkBox_hairspa, checkBox_colouring, checkBox_facial, checkBox_eyebrow, checkBox_massage;
    Button btn_update;
    public fragment_setting() {
    }

    public static fragment_setting newInstance(String param1, String param2) {
        fragment_setting fragment = new fragment_setting();
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
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        checkBox_hairstyle = root.findViewById(R.id.checkbox_hairstyle);
        checkBox_hairstyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("HairStyle");
                    } else {
                        list_services.remove("HairStyle");

                    }

                }
            }
        });
        checkBox_hairspa = root.findViewById(R.id.checkbox_hairspa);
        checkBox_hairspa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("HairSpa");
                    } else {
                        list_services.remove("HairSpa");

                    }

                }
            }
        });
        checkBox_colouring = root.findViewById(R.id.checkbox_colouring);
        checkBox_colouring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("Colouring");
                    } else {
                        list_services.remove("Colouring");

                    }

                }
            }
        });
        checkBox_facial = root.findViewById(R.id.checkbox_facial);
        checkBox_facial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("Facial");
                    } else {
                        list_services.remove("Facial");

                    }

                }
            }
        });
        checkBox_eyebrow = root.findViewById(R.id.checkbox_eyebrow);
        checkBox_eyebrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("EyeBrow");
                    } else {
                        list_services.remove("EyeBrow");

                    }

                }
            }
        });
        checkBox_massage = root.findViewById(R.id.checkbox_massage);
        checkBox_massage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isPressed()) {

                    if (isChecked) {

                        list_services.add("Massage");
                    } else {
                        list_services.remove("Massage");

                    }

                }
            }
        });


        btn_update=root.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_services(Helper.GetData(getActivity(),"user_id"),list_services);
            }
        });
        fetch_services(Helper.GetData(getActivity(), "email"));

        return root;
    }

    public void fetch_services(String email) {


        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {

                    Helper.stopLoader();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        for (DataSnapshot services_postSnapshot : childSnapshot.child("list_services").getChildren()) {


                            String service = services_postSnapshot.getValue().toString();

                            if (service.equals("HairStyle")) {

                                checkBox_hairstyle.setChecked(true);
                            } else if (service.equals("HairSpa")) {

                                checkBox_hairspa.setChecked(true);
                            } else if (service.equals("Colouring")) {

                                checkBox_colouring.setChecked(true);
                            } else if (service.equals("Facial")) {

                                checkBox_facial.setChecked(true);
                            } else if (service.equals("EyeBrow")) {

                                checkBox_eyebrow.setChecked(true);
                            } else if (service.equals("Massage")) {
                                checkBox_massage.setChecked(true);

                            }
                            list_services.add(service);

                        }


                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    Helper.stopLoader();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Helper.stopLoader();
            }
        });
    }

    private void update_services(String user_id, ArrayList<String> list_services) {


        databaseReference.child(user_id).child("list_services").setValue(list_services).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Helper.stopLoader();
                    Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

