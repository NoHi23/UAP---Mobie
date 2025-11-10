package com.uap.ui.fragment.student;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
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
import com.uap.data.remote.response.student.AnnouncementResponse;
import com.uap.ui.adapters.student.StudentAnnouncementAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAnnouncementsFragment extends Fragment {

    private SwipeRefreshLayout swipe;
    private ProgressBar progress;
    private TextView tvEmpty;
    private androidx.recyclerview.widget.RecyclerView rv;
    private StudentAnnouncementAdapter adapter;
    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private final SimpleDateFormat outFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_announcements, container, false);
        swipe = v.findViewById(R.id.swipeRefresh);
        progress = v.findViewById(R.id.progress);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        rv = v.findViewById(R.id.rvAnnouncements);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StudentAnnouncementAdapter(this::openDetail);
        rv.setAdapter(adapter);

        swipe.setOnRefreshListener(this::loadData);
        loadData();

        return v;
    }

    private void loadData() {
        showLoading(true);
        ApiService.getSOService().getStudentAnnouncements()
                .enqueue(new Callback<AnnouncementResponse>() {
                    @Override
                    public void onResponse(Call<AnnouncementResponse> call, Response<AnnouncementResponse> response) {
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
                    public void onFailure(Call<AnnouncementResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Không tải được thông báo", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (!b && swipe.isRefreshing()) swipe.setRefreshing(false);
    }

    private void openDetail(AnnouncementResponse.Announcement a) {
        if (getContext() == null) return;
        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_announcement_detail);

        TextView tvTitle = d.findViewById(R.id.tvTitle);
        TextView tvMeta = d.findViewById(R.id.tvMeta);
        TextView tvContent = d.findViewById(R.id.tvContent);
        View btnClose = d.findViewById(R.id.btnClose);

        tvTitle.setText(a.getTitle());

        String timeStr = "";
        try {
            Date dt = iso.parse(a.getCreatedAt());
            timeStr = outFmt.format(dt);
        } catch (Exception ignored) {}
        tvMeta.setText((a.getPostBy() != null ? a.getPostBy() : "") + " • " + timeStr);

        tvContent.setText(Html.fromHtml(a.getContent() == null ? "" : a.getContent(),
                Html.FROM_HTML_MODE_LEGACY));

        btnClose.setOnClickListener(v -> d.dismiss());

        if (d.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            d.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        d.show();
    }
}
