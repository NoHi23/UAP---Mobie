package com.uap.ui.fragments.lecturer;

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
import com.uap.data.remote.request.lecturer.GetLectureScheduleRequest;
import com.uap.data.remote.response.lecturer.LecturerScheduleResponse;
import com.uap.domain.model.LecturerScheduleItem;
import com.uap.ui.adapters.lecturer.LecturerScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerScheduleFragment extends Fragment {

    private RecyclerView rv;
    private ProgressBar progress;
    private SwipeRefreshLayout swipe;
    private final List<LecturerScheduleItem> data = new ArrayList<>();
    private LecturerScheduleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lecturer_schedule, container, false);

        rv = v.findViewById(R.id.rvLecturerSchedule);
        progress = v.findViewById(R.id.progress);
        swipe = v.findViewById(R.id.swipeRefresh);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LecturerScheduleAdapter(data);
        adapter.setOnItemClickListener(item -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, ScheduleDetailBottomSheetFragment.newInstance(item.get_id(), item.getClassId().get_id()))
                    .addToBackStack(null)
                    .commit();
        });
        rv.setAdapter(adapter);

        swipe.setOnRefreshListener(this::loadSchedule);

        loadSchedule();

        return v;
    }

    private void loadSchedule() {
        showLoading(true);
        ApiService.getSOService()
                .getLecturerSchedules(new GetLectureScheduleRequest("",""))
                .enqueue(new Callback<LecturerScheduleResponse>() {
                    @Override
                    public void onResponse(Call<LecturerScheduleResponse> call,
                                           Response<LecturerScheduleResponse> response) {
                        showLoading(false);
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            data.clear();
                            data.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Không tải được thời khóa biểu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LecturerScheduleResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean b) {
        if (progress != null) progress.setVisibility(b ? View.VISIBLE : View.GONE);
        if (swipe != null && swipe.isRefreshing() && !b) swipe.setRefreshing(false);
    }
}
