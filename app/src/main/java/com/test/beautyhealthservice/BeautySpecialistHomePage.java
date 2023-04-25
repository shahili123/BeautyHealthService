package com.test.beautyhealthservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class BeautySpecialistHomePage extends AppCompatActivity {
    private final static int ID_SETTING = 1;
    private final static int ID_MESSAGES = 2;
    private final static int ID_PROFILE = 3;
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_specialist_home_page);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_SETTING, R.drawable.ic_baseline_settings_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGES, R.drawable.ic_baseline_message_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_baseline_person_24));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //    Toast.makeText(ActivityHome.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //   Toast.makeText(ActivityHome.this, "showing item : " + item.getId(), Toast.LENGTH_SHORT).show();

                String name;
                switch (item.getId()) {
                    case ID_SETTING:
                        name = "HOME";
                    //    changeFragment(fragment_map.newInstance("",""),"setting");

                        break;
                    case ID_MESSAGES:



                        break;
                    case ID_PROFILE:


                        break;
                    default:
                        name = "";
                }

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //    Toast.makeText(ActivityHome.this, "reselected item : " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.show(ID_SETTING,true);

    }
}