package uk.ac.tees.w9598552.beautyhealthservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;


import uk.ac.tees.w9598552.beautyhealthservice.Fragments.BeautySpecialistFragments.fragment_message;
import uk.ac.tees.w9598552.beautyhealthservice.Fragments.BeautySpecialistFragments.fragment_profile;
import uk.ac.tees.w9598552.beautyhealthservice.Fragments.BeautySpecialistFragments.fragment_setting;

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
            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                switch (item.getId()) {
                    case ID_SETTING:
                        changeFragment(fragment_setting.newInstance("",""),"setting");

                        break;
                    case ID_MESSAGES:
                        changeFragment(fragment_message.newInstance("",""),"chats");

                        break;
                    case ID_PROFILE:
                        changeFragment(fragment_profile.newInstance("",""),"profile");

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
        bottomNavigation.show(ID_SETTING,true);

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