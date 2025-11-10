package com.uap.ui.fragments.student;


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
import com.uap.data.remote.response.student.TransactionHistoryResponse;
import com.uap.ui.adapters.student.StudentTransactionAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTransactionsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progress;
    private TextView tvEmpty;
    private androidx.recyclerview.widget.RecyclerView rv;
    private StudentTransactionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_transactions, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        progress = v.findViewById(R.id.progress);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        rv = v.findViewById(R.id.rvTransactions);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentTransactionAdapter();
        rv.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        loadData();

        return v;
    }

    private void loadData() {
        showLoading(true);
        ApiService.getSOService()
                .getStudentTransactions()
                .enqueue(new Callback<TransactionHistoryResponse>() {
                    @Override
                    public void onResponse(Call<TransactionHistoryResponse> call,
                                           Response<TransactionHistoryResponse> response) {
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
                    public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Không tải được giao dịch", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (!b && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
