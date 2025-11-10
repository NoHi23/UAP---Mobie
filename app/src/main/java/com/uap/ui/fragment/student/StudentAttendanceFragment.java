package com.uap.ui.fragment.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.ui.adapters.student.StudentAttendanceAdapter;
import com.uap.data.remote.response.student.StudentAttendanceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAttendanceFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progress;
    private TextView tvEmpty;
    private androidx.recyclerview.widget.RecyclerView rv;
    private StudentAttendanceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_attendance, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        progress = v.findViewById(R.id.progress);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        rv = v.findViewById(R.id.rvAttendance);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAttendanceAdapter();
        rv.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        loadData();
        return v;
    }

    private void loadData() {
        showLoading(true);
        ApiService.getSOService()
                .getStudentAttendance() // bạn thêm hàm này trong ApiService
                .enqueue(new Callback<StudentAttendanceResponse>() {
                    @Override
                    public void onResponse(Call<StudentAttendanceResponse> call,
                                           Response<StudentAttendanceResponse> response) {
                        showLoading(false);
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<StudentAttendanceResponse.AttendanceSubject> flattened = flatten(response.body().getData());
                            if (flattened.isEmpty()) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                tvEmpty.setVisibility(View.GONE);
                            }
                            adapter.setData(flattened);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentAttendanceResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Lỗi tải điểm danh", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (!b && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    // gộp các subject của từng kỳ thành 1 list phẳng để show
    private List<StudentAttendanceResponse.AttendanceSubject> flatten(List<StudentAttendanceResponse.AttendanceSemester> data) {
        List<StudentAttendanceResponse.AttendanceSubject> result = new ArrayList<>();
        if (data == null) return result;
        for (StudentAttendanceResponse.AttendanceSemester sem : data) {
            if (sem.getSubjects() != null) {
                result.addAll(sem.getSubjects());
            }
        }
        return result;
    }
}
