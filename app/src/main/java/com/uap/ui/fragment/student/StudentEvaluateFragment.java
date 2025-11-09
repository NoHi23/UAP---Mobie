package com.uap.ui.fragment.student;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.uap.data.remote.request.student.StudentEvaluateRequest;
import com.uap.data.remote.response.student.StudentClassListResponse;
import com.uap.data.remote.response.student.StudentEvaluateResponse;
import com.uap.ui.adapters.student.StudentEvaluateAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentEvaluateFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progress;
    private TextView tvEmpty;
    private androidx.recyclerview.widget.RecyclerView rv;
    private StudentEvaluateAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_evaluate, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        progress = v.findViewById(R.id.progress);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        rv = v.findViewById(R.id.rvClasses);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentEvaluateAdapter(this::openEvaluateDialog);
        rv.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadClasses);

        loadClasses();

        return v;
    }

    private void loadClasses() {
        showLoading(true);
        ApiService.getSOService()
                .getStudentClasses()
                .enqueue(new Callback<StudentClassListResponse>() {
                    @Override
                    public void onResponse(Call<StudentClassListResponse> call,
                                           Response<StudentClassListResponse> response) {
                        showLoading(false);
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<StudentClassListResponse.StudentClassItem> list = response.body().getData();
                            if (list == null || list.isEmpty()) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            } else {
                                tvEmpty.setVisibility(View.GONE);
                            }
                            adapter.setData(list);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentClassListResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Không tải được danh sách lớp", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (!b && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void openEvaluateDialog(StudentClassListResponse.StudentClassItem item) {
        if (getContext() == null) return;
        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_student_evaluate);

        TextView tvTitle = d.findViewById(R.id.tvTitle);
        TextView tvClassInfo = d.findViewById(R.id.tvClassInfo);
        EditText edtCriteria = d.findViewById(R.id.edtCriteria);
        EditText edtComment = d.findViewById(R.id.edtComment);
        View btnCancel = d.findViewById(R.id.btnCancel);
        View btnSubmit = d.findViewById(R.id.btnSubmit);

        tvTitle.setText("Đánh giá " + (item.getSubjectId() != null ? item.getSubjectId().getSubjectName() : ""));
        tvClassInfo.setText(item.getClassName()
                + (item.getLecturerId() != null ? " • GV: " + item.getLecturerId().getFullName().trim() : ""));

        btnCancel.setOnClickListener(v -> d.dismiss());

        btnSubmit.setOnClickListener(v -> {
            String criteria = edtCriteria.getText().toString().trim();
            String comment = edtComment.getText().toString().trim();

            if (TextUtils.isEmpty(criteria)) {
                edtCriteria.setError("Nhập tiêu chí");
                return;
            }
            if (TextUtils.isEmpty(comment)) {
                edtComment.setError("Nhập nhận xét");
                return;
            }

            sendEvaluate(item.get_id(), criteria, comment, d);
        });

        if (d.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            d.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        d.show();
    }

    private void sendEvaluate(String classId, String criteria, String comment, Dialog dialog) {
        ApiService.getSOService()
                .sendStudentEvaluation(new StudentEvaluateRequest(classId, criteria, comment))
                .enqueue(new Callback<StudentEvaluateResponse>() {
                    @Override
                    public void onResponse(Call<StudentEvaluateResponse> call,
                                           Response<StudentEvaluateResponse> response) {
                        if (!isAdded()) return;

                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    response.body() != null && response.body().getMessage() != null
                                            ? response.body().getMessage()
                                            : "Đã gửi đánh giá",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(),
                                    response.body() != null && response.body().getMessage() != null
                                            ? response.body().getMessage()
                                            : "Gửi đánh giá thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentEvaluateResponse> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
