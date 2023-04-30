package com.test.beautyhealthservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.test.beautyhealthservice.Fragments.BeautySpecialistFragments.fragment_setting;
import com.test.beautyhealthservice.Fragments.UserFragments.fragment_user_home;
import com.test.beautyhealthservice.Fragments.UserFragments.fragment_user_messages;
import com.test.beautyhealthservice.Fragments.UserFragments.fragment_user_profile;
import com.test.beautyhealthservice.Fragments.UserFragments.fragment_user_recipie;

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
        getSupportActionBar().hide();
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
                        changeFragment(fragment_user_home.newInstance("",""),"home");

                        break;
                    case ID_MESSAGES:
                        changeFragment(fragment_user_messages.newInstance("",""),"messages");

                        break;

                    case ID_RECEPIE:
                        changeFragment(fragment_user_recipie.newInstance("",""),"recipie");

                        break;

                    case ID_PROFILE:
                        changeFragment(fragment_user_profile.newInstance("",""),"profile");

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
        changeFragment(fragment_user_home.newInstance("",""),"home");

    }

    public void changeFragment(Fragment fragment, String tagFragmentName) {

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.container, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}