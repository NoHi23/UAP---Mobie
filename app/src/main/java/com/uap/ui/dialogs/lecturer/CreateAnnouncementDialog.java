package com.uap.ui.dialogs.lecturer;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.lecturer.CreateAnnouncementRequest;
import com.uap.data.remote.response.lecturer.CreateAnnouncementResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAnnouncementDialog extends DialogFragment {

    private static final String ARG_SCHEDULE_ID = "schedule_id";
    private String scheduleId;

    public interface OnCreatedListener {
        void onCreated();
    }

    private OnCreatedListener listener;

    public static CreateAnnouncementDialog newInstance(String scheduleId) {
        CreateAnnouncementDialog dialog = new CreateAnnouncementDialog();
        Bundle args = new Bundle();
        args.putString(ARG_SCHEDULE_ID, scheduleId);
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnCreatedListener(OnCreatedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_create_announcement, container, false);
        scheduleId = getArguments() != null ? getArguments().getString(ARG_SCHEDULE_ID) : null;

        EditText edtTitle = v.findViewById(R.id.edtTitle);
        EditText edtContent = v.findViewById(R.id.edtContent);
        MaterialButton btnSave = v.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                edtTitle.setError("Nhập tiêu đề");
                return;
            }

            ApiService.getSOService()
                    .createScheduleAnnouncement(new CreateAnnouncementRequest(scheduleId,title, content))
                    .enqueue(new Callback<CreateAnnouncementResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CreateAnnouncementResponse> call,
                                               @NonNull Response<CreateAnnouncementResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Đã tạo thông báo", Toast.LENGTH_SHORT).show();
                                if (listener != null) listener.onCreated();
                                dismiss();
                            } else {
                                Toast.makeText(requireContext(), "Tạo thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CreateAnnouncementResponse> call, @NonNull Throwable t) {
                            Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
