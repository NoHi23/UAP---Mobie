package com.uap.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull; // <- dùng androidx

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uap.R;
import com.uap.auth.AuthManager;
import com.uap.ui.fragment.HomeFragment;
import com.uap.ui.fragment.ScheduleFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private MaterialToolbar toolbar;
    @Override
    protected void onStart() {
        super.onStart();
        if (!AuthManager.isLoggedIn(this)) {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment f = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                f = new ScheduleFragment();
                setTitle("Lịch học");
                return true;
            } else if (itemId == R.id.nav_timetable) {
                //startActivity(new Intent(MainActivity.this, TimetableActivity.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Mở màn hình thông báo
                return true;
            }
            // Thêm các case khác cho các mục còn lại
            return false;
        });
    }

    private void replace(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}