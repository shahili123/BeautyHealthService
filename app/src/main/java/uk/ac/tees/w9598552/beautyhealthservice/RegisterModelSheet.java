package uk.ac.tees.w9598552.beautyhealthservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class RegisterModelSheet extends BottomSheetDialogFragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View mView= LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_register, container, false);

        Button btn_user = mView.findViewById(R.id.btn_user);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),RegisterActivity.class));
            }
        });

        Button btn_beauty_specialist = mView.findViewById(R.id.btn_beauty);
        btn_beauty_specialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),RegisterBeautySpecialistActivity.class));
            }
        });
        return mView;
    }


}

