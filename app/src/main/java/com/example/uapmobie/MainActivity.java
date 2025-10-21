package com.example.uapmobie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.uapmobie.TodayScheduleAdapter;
import com.example.uapmobie.ClassSlot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvWelcomeMessage, tvStudentId, tvGpa;
    private RecyclerView rvTodaySchedule;
    private CardView cvTimetable, cvExamSchedule, cvResults, cvInvoices;
    private BottomNavigationView bottomNavigationView;

    private TodayScheduleAdapter scheduleAdapter;
    private List<ClassSlot> todayClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        loadUserData();

        setupTodaySchedule();

        setupFunctionButtons();

        setupBottomNavigation();
    }

    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage); // Giả sử bạn có id này
        tvGpa = findViewById(R.id.tvGpa); // Giả sử bạn có id này
        rvTodaySchedule = findViewById(R.id.rvTodaySchedule); // Giả sử bạn có id này

        cvTimetable = findViewById(R.id.cvTimetable); // Đặt ID cho các card chức năng
        cvExamSchedule = findViewById(R.id.cvExamSchedule);
        cvResults = findViewById(R.id.cvResults);
        cvInvoices = findViewById(R.id.cvInvoices);

        bottomNavigationView = findViewById(R.id.bottom_navigation); // Giả sử bạn có id này
    }

    private void loadUserData() {
        // --- PHẦN NÀY SẼ LẤY DỮ LIỆU TỪ API/DATABASE ---
        // Dưới đây là dữ liệu giả để minh họa
        tvWelcomeMessage.setText("Xin chào, Phạm Đức Thắng");
        tvGpa.setText("8.24");
        // Cập nhật các thông tin khác tương tự
    }

    private void setupTodaySchedule() {
        todayClasses = new ArrayList<>();
        // --- PHẦN NÀY SẼ LẤY DỮ LIỆU TỪ API/DATABASE ---
        // Thêm dữ liệu giả để hiển thị
        todayClasses.add(new ClassSlot("Giao tiếp kinh doanh", "2 Tiết", "Trương Thị Nam Thắng", "A2-411", "ĐH KTQD", 5, 6));
        todayClasses.add(new ClassSlot("Kinh tế vĩ mô", "3 Tiết", "Nguyễn Văn A", "D1-101", "ĐH KTQD", 7, 9));

        scheduleAdapter = new TodayScheduleAdapter(this, todayClasses);
        rvTodaySchedule.setLayoutManager(new LinearLayoutManager(this));
        rvTodaySchedule.setAdapter(scheduleAdapter);
    }

    private void setupFunctionButtons() {
        cvTimetable.setOnClickListener(v -> {
            // Thay TimetableActivity.class bằng tên Activity tương ứng của bạn
            startActivity(new Intent(MainActivity.this, TimetableActivity.class));
        });

        cvExamSchedule.setOnClickListener(v -> {
            // Thay ExamScheduleActivity.class bằng tên Activity tương ứng của bạn
            startActivity(new Intent(MainActivity.this, ExamScheduleActivity.class));
        });

        cvResults.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Kết quả học tập", Toast.LENGTH_SHORT).show();
        });

        cvInvoices.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Hóa đơn", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBottomNavigation() {
        // Đặt mục "NEU" (home) được chọn mặc định
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Đang ở màn hình chính rồi
                return true;
            } else if (itemId == R.id.nav_timetable) {
                startActivity(new Intent(MainActivity.this, TimetableActivity.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Mở màn hình thông báo
                return true;
            }
            // Thêm các case khác cho các mục còn lại
            return false;
        });
    }
}