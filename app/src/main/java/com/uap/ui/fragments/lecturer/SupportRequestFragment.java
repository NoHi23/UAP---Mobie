package com.uap.ui.fragments.lecturer;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.lecturer.CreateSupportRequest;
import com.uap.data.remote.response.BasicResponse;
import com.uap.data.remote.response.lecturer.SupportRequestListResponse;
import com.uap.ui.adapters.lecturer.SupportRequestAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportRequestFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private SupportRequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_support_requests, container, false);

        swipeRefresh = v.findViewById(R.id.swipeRefresh);
        RecyclerView rv = v.findViewById(R.id.rvSupportRequests);
        View fabAdd = v.findViewById(R.id.fabAdd);

        adapter = new SupportRequestAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadData);
        fabAdd.setOnClickListener(view -> openCreateDialog());

        loadData();

        return v;
    }

    private void loadData() {
        swipeRefresh.setRefreshing(true);
        ApiService.getSOService()
                .getSupportRequests()
                .enqueue(new Callback<SupportRequestListResponse>() {
                    @Override
                    public void onResponse(Call<SupportRequestListResponse> call,
                                           Response<SupportRequestListResponse> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<SupportRequestListResponse> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getContext(), "Lỗi tải danh sách", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openCreateDialog() {
        if (getContext() == null) return;

        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_create_support_request);

        EditText edtRequest = d.findViewById(R.id.edtRequest);
        Button btnSend = d.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String content = edtRequest.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                edtRequest.setError("Nhập nội dung");
                return;
            }

            ApiService.getSOService()
                    .createSupportRequest(new CreateSupportRequest(content))
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Đã gửi yêu cầu", Toast.LENGTH_SHORT).show();
                                d.dismiss();
                                loadData();
                            } else {
                                Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // cho dialog rộng ~90%
        if (d.getWindow() != null) {
            int w = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
            d.getWindow().setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        d.show();
    }
}
