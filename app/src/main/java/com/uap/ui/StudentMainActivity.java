package com.uap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull; // <- dùng androidx

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uap.R;
import com.uap.data.local.AuthManager;
import com.uap.ui.fragments.AnnouncementsFragment;
import com.uap.ui.fragments.student.StudentExamFragment;
import com.uap.ui.fragments.student.StudentHomeFragment;
import com.uap.ui.fragments.student.StudentProfileFragment;
import com.uap.ui.fragments.student.StudentScheduleFragment;

public class StudentMainActivity extends AppCompatActivity {
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
                    .replace(R.id.frame_container, new StudentHomeFragment())
                    .commit();
        }

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment f = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                f = new StudentHomeFragment();
                setTitle("Trang chủ");
            } else if (itemId == R.id.nav_timetable) {
                f = new StudentScheduleFragment();
                setTitle("Lịch học");
            } else if (itemId == R.id.nav_exam_schedule) {
                f = new StudentExamFragment();
                setTitle("Lịch thi");
            } else if (itemId == R.id.nav_account) {
                f = new StudentProfileFragment();
                setTitle("Thông tin sinh viên");
            } else if (itemId == R.id.nav_notifications) {
                f = new AnnouncementsFragment();
                setTitle("Thông báo");
            }
            if (f != null) {
                replace(f);
                return true;
            }
            return false;
        });
    }

    private void replace(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}