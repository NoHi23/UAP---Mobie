package com.uap.ui.dialogs.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.student.StudentClassmateResponse;
import com.uap.data.remote.response.student.StudentNotificationResponse;
import com.uap.domain.model.TimetableItem;
import com.uap.ui.adapters.student.StudentClassmateAdapter;
import com.uap.ui.adapters.student.StudentNotificationAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentSlotDetailDialog extends BottomSheetDialog {

    private final TimetableItem slot;

    // cờ để biết 2 api đã xong chưa
    private boolean notifyLoaded = false;
    private boolean memberLoaded = false;

    public StudentSlotDetailDialog(@NonNull android.content.Context context, TimetableItem slot) {
        super(context, R.style.AppBottomSheetDialogTheme);
        this.slot = slot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_student_slot_detail, null);
        setContentView(v);

        // full screen-ish
        getBehavior().setPeekHeight(
                getContext().getResources().getDisplayMetrics().heightPixels
        );
        getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        TextView tvInfo = v.findViewById(R.id.tvInfo);
        TextView tvSlotSubject = v.findViewById(R.id.tvSlotSubject);
        TextView tvSlotRoom = v.findViewById(R.id.tvSlotRoom);
        TextView tvSlotTime = v.findViewById(R.id.tvSlotTime);
        TextView tvEmptyNotify = v.findViewById(R.id.tvEmptyNotify);
        TextView tvEmptyMembers = v.findViewById(R.id.tvEmptyMembers);
        ProgressBar progress = v.findViewById(R.id.progress);
        RecyclerView rvNotifications = v.findViewById(R.id.rvNotifications);
        RecyclerView rvMembers = v.findViewById(R.id.rvMembers);
        View btnClose = v.findViewById(R.id.btnClose);

        setCancelable(true);

        // ẩn nội dung lúc đầu
        rvNotifications.setVisibility(View.GONE);
        rvMembers.setVisibility(View.GONE);
        tvEmptyNotify.setVisibility(View.GONE);
        tvEmptyMembers.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // fill info
        tvInfo.setText("Chi tiết buổi học");
        tvSlotSubject.setText(slot.getSubjectId().getSubjectName());
        tvSlotRoom.setText("Phòng: " + slot.getRoomId().getRoomName());
        tvSlotTime.setText("Thời gian: " + slot.getStartTime() + " - " + slot.getEndTime());

        btnClose.setOnClickListener(v1 -> dismiss());

        // CALL 1: notifications
        ApiService.getSOService().getSlotNotifications(slot.get_id()).enqueue(new Callback<StudentNotificationResponse>() {
            @Override
            public void onResponse(Call<StudentNotificationResponse> call, Response<StudentNotificationResponse> response) {
                notifyLoaded = true;
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    rvNotifications.setAdapter(new StudentNotificationAdapter(response.body().getData()));
                    rvNotifications.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyNotify.setVisibility(View.VISIBLE);
                }
                checkAllLoaded(progress, rvNotifications, rvMembers);
            }

            @Override
            public void onFailure(Call<StudentNotificationResponse> call, Throwable t) {
                notifyLoaded = true;
                tvEmptyNotify.setVisibility(View.VISIBLE);
                checkAllLoaded(progress, rvNotifications, rvMembers);
            }
        });

        // CALL 2: classmates
        ApiService.getSOService().getClassmates(slot.getClassId().get_id()).enqueue(new Callback<StudentClassmateResponse>() {
            @Override
            public void onResponse(Call<StudentClassmateResponse> call, Response<StudentClassmateResponse> response) {
                memberLoaded = true;
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    rvMembers.setAdapter(new StudentClassmateAdapter(response.body().getData()));
                    rvMembers.setVisibility(View.VISIBLE);
                } else {
                    tvEmptyMembers.setVisibility(View.VISIBLE);
                }
                checkAllLoaded(progress, rvNotifications, rvMembers);
            }

            @Override
            public void onFailure(Call<StudentClassmateResponse> call, Throwable t) {
                memberLoaded = true;
                tvEmptyMembers.setVisibility(View.VISIBLE);
                checkAllLoaded(progress, rvNotifications, rvMembers);
            }
        });
    }

    private void checkAllLoaded(ProgressBar progress,
                                RecyclerView rvNotifications,
                                RecyclerView rvMembers) {
        if (notifyLoaded && memberLoaded) {
            progress.setVisibility(View.GONE);
        }
    }
}
