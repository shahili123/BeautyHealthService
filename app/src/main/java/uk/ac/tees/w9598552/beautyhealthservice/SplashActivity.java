package uk.ac.tees.w9598552.beautyhealthservice;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

}
