package com.uap.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.uap.R;
import com.uap.data.local.AuthManager;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            if (AuthManager.isLoggedIn(this)) {
                if(AuthManager.getRole(this).equalsIgnoreCase("lecturer") || AuthManager.getRole(this).equalsIgnoreCase("lecture")){
                    startActivity(new Intent(this, TeacherMainActivity.class));
                }else{
                    startActivity(new Intent(this, StudentMainActivity.class));
                }
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DURATION);
    }
}
