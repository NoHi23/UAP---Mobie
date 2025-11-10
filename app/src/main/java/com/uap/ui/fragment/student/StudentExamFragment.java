package com.uap.ui.fragment.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.student.StudentExamResponse;
import com.uap.domain.model.ExamItem;
import com.uap.ui.adapters.student.StudentExamAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentExamFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_exam, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.progress);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        txtEmpty = v.findViewById(R.id.txtEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(this::loadExams);

        loadExams();
        return v;
    }

    private void loadExams() {
        showLoading(true);
        ApiService.getSOService().getStudentExams().enqueue(new Callback<StudentExamResponse>() {
            @Override
            public void onResponse(Call<StudentExamResponse> call, Response<StudentExamResponse> response) {
                showLoading(false);
                if (!response.isSuccessful() || response.body() == null) {
                    txtEmpty.setVisibility(View.VISIBLE);
                    return;
                }

                List<ExamItem> exams = response.body().getExamSchedule();
                if (exams == null || exams.isEmpty()) {
                    txtEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    txtEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new StudentExamAdapter(exams));
                }
            }

            @Override
            public void onFailure(Call<StudentExamResponse> call, Throwable t) {
                showLoading(false);
                Log.d("Exam", "Error: " + t.getMessage());
                txtEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoading(boolean b) {
        progressBar.setVisibility(b ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }
}
