package com.uap.ui.fragments.student;


import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.student.SubmitStudentRequest;
import com.uap.data.remote.response.student.RequestResponse;
import com.uap.ui.adapters.student.StudentRequestAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRequestsFragment extends Fragment {

    private SwipeRefreshLayout swipe;
    private ProgressBar progress;
    private TextView tvEmpty;
    private androidx.recyclerview.widget.RecyclerView rv;
    private StudentRequestAdapter adapter;
    private View btnAddRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_requests, container, false);
        swipe = v.findViewById(R.id.swipeRefresh);
        progress = v.findViewById(R.id.progress);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        rv = v.findViewById(R.id.rvRequests);
        btnAddRequest = v.findViewById(R.id.btnAddRequest);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentRequestAdapter();
        rv.setAdapter(adapter);

        swipe.setOnRefreshListener(this::loadData);
        btnAddRequest.setOnClickListener(view -> showCreateDialog());

        loadData();
        return v;
    }

    private void loadData() {
        showLoading(true);
        ApiService.getSOService().getStudentRequests()
                .enqueue(new Callback<RequestResponse>() {
                    @Override
                    public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                        showLoading(false);
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            adapter.setData(response.body().getData());
                            tvEmpty.setVisibility(response.body().getData().isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RequestResponse> call, Throwable t) {
                        showLoading(false);
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Không tải được danh sách đơn", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (!b && swipe.isRefreshing()) swipe.setRefreshing(false);
    }

    private void showCreateDialog() {
        View form = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_request, null);
        Spinner spType = form.findViewById(R.id.spRequestType);
        EditText etTitle = form.findViewById(R.id.etTitle);
        EditText etDesc = form.findViewById(R.id.etDescription);

        new AlertDialog.Builder(getContext())
                .setTitle("Gửi đơn mới")
                .setView(form)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    String type = spType.getSelectedItem().toString();
                    String title = etTitle.getText().toString().trim();
                    String desc = etDesc.getText().toString().trim();

                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ApiService.getSOService().submitStudentRequest(new SubmitStudentRequest(type, title, desc))
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(), "Đã gửi đơn", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
