package com.test.beautyhealthservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class UserHomePage extends AppCompatActivity {
    private final static int ID_HOME = 1;
    private final static int ID_MESSAGES = 2;
    private final static int ID_RECEPIE = 3;
    private final static int ID_PROFILE = 4;

    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_MESSAGES, R.drawable.ic_baseline_message_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_RECEPIE, R.drawable.ic_baseline_food_bank_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_PROFILE, R.drawable.ic_baseline_person_24));
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                switch (item.getId()) {
                    case ID_HOME:

                        break;
                    case ID_MESSAGES:
                        break;

                    case ID_RECEPIE:
                        break;

                    case ID_PROFILE:
                        break;
                    default:
                }

            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });
        bottomNavigation.show(ID_HOME,true);

    }
}