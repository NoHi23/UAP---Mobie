package com.uap.ui.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.student.StudentGradeResponse;
import com.uap.domain.model.GradeItem;
import com.uap.domain.model.SubjectGrade;
import com.uap.ui.adapters.student.StudentGradesAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentGradesFragment extends Fragment {

    private RecyclerView rv;
    private ProgressBar progress;
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_grades, container, false);
        rv = v.findViewById(R.id.rvGrades);
        progress = v.findViewById(R.id.progress);
        swipe = v.findViewById(R.id.swipeRefresh);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if (swipe != null) {
            swipe.setOnRefreshListener(this::loadGrades);
        }

        loadGrades();

        return v;
    }

    private void loadGrades() {
        showLoading(true);
        ApiService.getSOService()
                .getStudentGrades()
                .enqueue(new Callback<StudentGradeResponse>() {
                    @Override
                    public void onResponse(Call<StudentGradeResponse> call,
                                           Response<StudentGradeResponse> response) {
                        if (!isAdded()) {
                            showLoading(false);
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null) {
                            List<SubjectGrade> list = groupBySubject(response.body().getGrades());
                            rv.setAdapter(new StudentGradesAdapter(getContext(), list));
                        } else {
                            Toast.makeText(getContext(), "Không lấy được điểm", Toast.LENGTH_SHORT).show();
                        }
                        showLoading(false);
                    }

                    @Override
                    public void onFailure(Call<StudentGradeResponse> call, Throwable t) {
                        if (!isAdded()) return;
                        showLoading(false);
                        Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        if (progress != null) progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (swipe != null && swipe.isRefreshing() && !b) swipe.setRefreshing(false);
    }

    /**
     * group list component thành từng môn + tính tổng = sum(score * weight / 100)
     */
    private List<SubjectGrade> groupBySubject(List<GradeItem> items) {
        Map<String, List<GradeItem>> map = new HashMap<>();
        Map<String, String> subjectNameMap = new HashMap<>();
        Map<String, String> subjectCodeMap = new HashMap<>();

        for (GradeItem gi : items) {
            String subId = gi.getSubjectId().get_id();
            if (!map.containsKey(subId)) {
                map.put(subId, new ArrayList<>());
                subjectNameMap.put(subId, gi.getSubjectId().getSubjectName());
                subjectCodeMap.put(subId, gi.getSubjectId().getSubjectCode());
            }
            map.get(subId).add(gi);
        }

        List<SubjectGrade> result = new ArrayList<>();
        for (String subId : map.keySet()) {
            List<GradeItem> comps = map.get(subId);
            double finalScore = 0;
            for (GradeItem c : comps) {
                double w = c.getComponentId() != null ? c.getComponentId().getWeightPercentage() : 0;
                finalScore += c.getScore() * (w / 100.0);
            }
            result.add(new SubjectGrade(
                    subId,
                    subjectNameMap.get(subId),
                    subjectCodeMap.get(subId),
                    comps,
                    finalScore
            ));
        }

        return result;
    }
}
