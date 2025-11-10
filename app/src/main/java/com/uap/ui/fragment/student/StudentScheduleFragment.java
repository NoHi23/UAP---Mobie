package com.uap.ui.fragment.student;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.student.StudentTimetableResponse;
import com.uap.domain.model.TimetableItem;

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

public class StudentScheduleFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String[] tabTitles = {"Thứ 2","Thứ 3","Thứ 4","Thứ 5","Thứ 6","Thứ 7","Chủ nhật"};
    private List<TimetableItem> allItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_schedule, container, false);
        tabLayout = v.findViewById(R.id.tabLayout);
        viewPager = v.findViewById(R.id.viewPager);
        progressBar = v.findViewById(R.id.progress);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);

        // kéo xuống để reload
        swipeRefreshLayout.setOnRefreshListener(this::reloadData);

        // lần đầu load
        reloadData();

        return v;
    }

    private void reloadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            loadTimetable();
        } else {
            loadTimetableLegacy();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadTimetable() {
        showLoading(true);
        ApiService.getSOService().getStudentTimetable().enqueue(new Callback<StudentTimetableResponse>() {
            @Override
            public void onResponse(Call<StudentTimetableResponse> call, Response<StudentTimetableResponse> response) {
                showLoading(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Log.d("Schedule", "response error");
                    return;
                }
                allItems = response.body().getData();
                setupPagerO();
            }

            @Override
            public void onFailure(Call<StudentTimetableResponse> call, Throwable t) {
                showLoading(false);
                Log.d("Schedule", "err: " + t.getMessage());
            }
        });
    }

    // dành cho máy api < 26
    private void loadTimetableLegacy() {
        showLoading(true);
        ApiService.getSOService().getStudentTimetable().enqueue(new Callback<StudentTimetableResponse>() {
            @Override
            public void onResponse(Call<StudentTimetableResponse> call, Response<StudentTimetableResponse> response) {
                showLoading(false);
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                allItems = response.body().getData();
                setupPagerLegacy();
            }

            @Override
            public void onFailure(Call<StudentTimetableResponse> call, Throwable t) {
                showLoading(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupPagerO() {
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                int dayWanted = position + 1; // 1 = Monday
                List<TimetableItem> list = filterByDayO(allItems, dayWanted);
                return StudentDayScheduleFragment.newInstance(list);
            }

            @Override
            public int getItemCount() {
                return 7;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
    }

    private void setupPagerLegacy() {
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                int dayWanted = position + 1; // 1 = Monday
                List<TimetableItem> list = filterByDayLegacy(allItems, dayWanted);
                return StudentDayScheduleFragment.newInstance(list);
            }

            @Override
            public int getItemCount() {
                return 7;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
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

    private void showLoading(boolean b) {
        if (progressBar != null) progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        if (viewPager != null) viewPager.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }
}
