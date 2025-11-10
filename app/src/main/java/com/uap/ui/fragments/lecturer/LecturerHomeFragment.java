package com.uap.ui.fragments.lecturer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uap.App;
import com.uap.R;
import com.uap.data.local.AuthManager;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.lecturer.GetLectureScheduleRequest;
import com.uap.data.remote.response.lecturer.LecturerScheduleResponse;
import com.uap.domain.model.LecturerScheduleItem;
import com.uap.domain.model.User;
import com.uap.ui.adapters.lecturer.LecturerScheduleAdapter;
import com.uap.ui.fragments.AnnouncementsFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerHomeFragment extends Fragment {
    private CardView cvTeachingSchedule , cvManageNotifications , cvClassList , cvSupport;
    private ImageView ivProfile;
    private TextView tvWelcomeMessage;

    private RecyclerView rvTodaySchedule;
    private LecturerScheduleAdapter adapter;
    private final List<LecturerScheduleItem> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lecturer_home, container, false);
        cvTeachingSchedule = v.findViewById(R.id.cvTeachingSchedule);
        if (cvTeachingSchedule != null) {
            cvTeachingSchedule.setOnClickListener(view -> {
                replaceFragment(new LecturerScheduleFragment(), true);
            });
        }

        cvManageNotifications = v.findViewById(R.id.cvManageNotifications);
        if (cvManageNotifications != null) {
            cvManageNotifications.setOnClickListener(view -> {
                replaceFragment(new AnnouncementsFragment(), true);
            });
        }
        cvClassList = v.findViewById(R.id.cvClassList);
        if (cvClassList != null) {
            cvClassList.setOnClickListener(view -> {
                replaceFragment(new LecturerClassesFragment(), true);
            });
        }
        cvSupport = v.findViewById(R.id.cvSupport);
        if (cvSupport != null) {
            cvSupport.setOnClickListener(view -> {
                replaceFragment(new SupportRequestFragment(), true);
            });
        }
        ivProfile = v.findViewById(R.id.ivProfile);
        if (ivProfile != null) {
            ivProfile.setOnClickListener(view -> {
                replaceFragment(new LecturerProfileFragment(), true);
            });
        }
        User user = AuthManager.getUser(App.getContext());
        tvWelcomeMessage = v.findViewById(R.id.tvWelcomeMessage);
        if (tvWelcomeMessage != null) {
            tvWelcomeMessage.setText("Xin chào, " + user.getName());
        }
        rvTodaySchedule = v.findViewById(R.id.rvTodaySchedule);
        rvTodaySchedule.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LecturerScheduleAdapter(data);

        rvTodaySchedule.setAdapter(adapter);

        loadSchedule(); // Gọi API


        return v;
    }
    private void loadSchedule() {

        // Lấy ngày hôm nay định dạng yyyy-MM-dd
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        ApiService.getSOService()
                .getLecturerSchedules(new GetLectureScheduleRequest(today, today))
                .enqueue(new Callback<LecturerScheduleResponse>() {
                    @Override
                    public void onResponse(Call<LecturerScheduleResponse> call,
                                           Response<LecturerScheduleResponse> response) {
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
                        if (!isAdded()) return;
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );

        transaction.replace(R.id.frame_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}