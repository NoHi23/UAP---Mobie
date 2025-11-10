package com.uap.ui.fragments.student;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.App;
import com.uap.R;
import com.uap.data.local.AuthManager;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.student.StudentTimetableResponse;
import com.uap.domain.model.TimetableItem;
import com.uap.domain.model.User;
import com.uap.ui.adapters.student.TimetableAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeFragment extends Fragment {

    private CardView btnGrade , cvExamSchedule , btnAttendance , btnInvoices;
    private TextView tvWelcomeMessage;
    private ImageView ivProfile;
    private RecyclerView rvTodaySchedule;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_home, container, false);

        btnGrade = v.findViewById(R.id.btnGrade);

        if (btnGrade != null) {
            btnGrade.setOnClickListener(view -> {
                replaceFragment(new StudentGradesFragment(), true);
            });
        }
        btnAttendance = v.findViewById(R.id.btnAttendance);
        if (btnAttendance != null) {
            btnAttendance.setOnClickListener(view -> {
                replaceFragment(new StudentAttendanceFragment(), true);
            });
        }

        cvExamSchedule= v.findViewById(R.id.cvExamSchedule);
        if (cvExamSchedule != null) {
            cvExamSchedule.setOnClickListener(view -> {
                replaceFragment(new StudentExamFragment(), true);
            });
        }
        btnInvoices = v.findViewById(R.id.btnInvoices);
        if (btnInvoices != null) {
            btnInvoices.setOnClickListener(view -> {
                replaceFragment(new StudentTransactionsFragment(), true);
            });
        }
        User user = AuthManager.getUser(App.getContext());
        tvWelcomeMessage = v.findViewById(R.id.tvWelcomeMessage);
        if (tvWelcomeMessage != null) {
            tvWelcomeMessage.setText("Xin chào, " + user.getName());
        }

        ivProfile = v.findViewById(R.id.ivProfile);
        if (ivProfile != null) {
            ivProfile.setOnClickListener(view -> {
                replaceFragment(new StudentProfileFragment(), true);
            });
        }
        rvTodaySchedule = v.findViewById(R.id.rvTodaySchedule);
        if (ivProfile != null) {
            loadTodayTimetable();
        }



        return v;
    }
    // Hàm helper xác định ngày trong tuần hiện tại (1=Mon, 7=Sun)
    private int getTodayNormalizedDayOfWeek() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDate.now().getDayOfWeek().getValue(); // Java 8+ (1=Mon, 7=Sun)
        } else {
            java.util.Calendar c = java.util.Calendar.getInstance();
            int dow = c.get(java.util.Calendar.DAY_OF_WEEK);
            // Chuyển Calendar (1=Sun, 2=Mon) sang chuẩn (1=Mon, 7=Sun)
            return (dow == java.util.Calendar.SUNDAY) ? 7 : (dow - 1);
        }
    }

    private void loadTodayTimetable() {
        ApiService.getSOService().getStudentTimetable().enqueue(new Callback<StudentTimetableResponse>() {
            @Override
            public void onResponse(Call<StudentTimetableResponse> call, Response<StudentTimetableResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                List<TimetableItem> allItems = response.body().getData();
                int todayDayOfWeek = getTodayNormalizedDayOfWeek();
                List<TimetableItem> todaySchedule;

                // Lọc dữ liệu theo phiên bản Android
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Cần @RequiresApi(api = Build.VERSION_CODES.O) cho hàm này
                    todaySchedule = filterByDayO(allItems, todayDayOfWeek);
                } else {
                    todaySchedule = filterByDayLegacy(allItems, todayDayOfWeek);
                }

                // Cấu hình và hiển thị dữ liệu đã lọc
                setupTodayScheduleRecyclerView(todaySchedule);
            }

            @Override
            public void onFailure(Call<StudentTimetableResponse> call, Throwable t) {
            }
        });
    }

    private void setupTodayScheduleRecyclerView(List<TimetableItem> schedule) {
        // *** DÒNG QUAN TRỌNG: Cấu hình LayoutManager cho phép cuộn ngang (HORIZONTAL) ***
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                App.getContext(),
                LinearLayoutManager.HORIZONTAL, // <-- Thiết lập cuộn ngang
                false
        );

        rvTodaySchedule.setLayoutManager(layoutManager);

        // Thay thế 'TimetableAdapter' bằng tên Adapter thực tế của bạn
        TimetableAdapter adapter = new TimetableAdapter(schedule);
        rvTodaySchedule.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<TimetableItem> filterByDayO(List<TimetableItem> source, int dayOfWeek) {
        List<TimetableItem> result = new ArrayList<>();
        for (TimetableItem item : source) {
            try {
                String dateStr = item.getDate().substring(0, 10); // "2025-11-04"
                LocalDate d = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                int dow = d.getDayOfWeek().getValue(); // 1=Mon
                if (dow == dayOfWeek) result.add(item);
            } catch (Exception ignored) {}
        }
        return result;
    }

    // phiên bản parse bằng SimpleDateFormat cho api thấp
    private List<TimetableItem> filterByDayLegacy(List<TimetableItem> source, int dayOfWeek) {
        List<TimetableItem> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (TimetableItem item : source) {
            try {
                String dateStr = item.getDate().substring(0, 10);
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTime(sdf.parse(dateStr));
                // Calendar.MONDAY = 2 → mình cần 1=Mon nên chuyển lại
                int dow = c.get(java.util.Calendar.DAY_OF_WEEK); // Sunday=1
                // chuyển Calendar -> 1=Mon..7=Sun
                int normalized = (dow == java.util.Calendar.SUNDAY) ? 7 : (dow - 1);
                if (normalized == dayOfWeek) result.add(item);
            } catch (ParseException ignored) {}
        }
        return result;
    }


    private void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );

        transaction.replace(R.id.frame_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
