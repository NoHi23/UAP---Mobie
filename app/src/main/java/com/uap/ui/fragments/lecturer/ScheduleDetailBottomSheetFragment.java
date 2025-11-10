package com.uap.ui.fragments.lecturer;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.lecturer.CreateAnnouncementRequest;
import com.uap.data.remote.request.lecturer.UpdateAttendanceRequest;
import com.uap.data.remote.response.BasicResponse;
import com.uap.data.remote.response.lecturer.CreateAnnouncementResponse;
import com.uap.data.remote.response.lecturer.ScheduleAnnouncementResponse;
import com.uap.data.remote.response.lecturer.ScheduleStudentsResponse;
import com.uap.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDetailBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_SCHEDULE_ID = "schedule_id";
    private static final String ARG_CLASS_ID = "class_id";

    private String scheduleId;
    private String classId;

    private RecyclerView rvAnn, rvStudents;
    private AnnAdapter annAdapter;
    private StudentAdapter studentAdapter;

    public static ScheduleDetailBottomSheetFragment newInstance(String scheduleId, String classId) {
        ScheduleDetailBottomSheetFragment s = new ScheduleDetailBottomSheetFragment();
        Bundle b = new Bundle();
        b.putString(ARG_SCHEDULE_ID, scheduleId);
        b.putString(ARG_CLASS_ID, classId);
        s.setArguments(b);
        return s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_schedule_detail, container, false);

        // lấy args an toàn
        Bundle args = getArguments();
        if (args != null) {
            scheduleId = args.getString(ARG_SCHEDULE_ID);
            classId = args.getString(ARG_CLASS_ID);
        }

        rvAnn = v.findViewById(R.id.rvAnnouncements);
        rvStudents = v.findViewById(R.id.rvStudents);
        MaterialButton btnAdd = v.findViewById(R.id.btnAddAnnouncement);

        rvAnn.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvStudents.setLayoutManager(new LinearLayoutManager(requireContext()));

        annAdapter = new AnnAdapter();
        rvAnn.setAdapter(annAdapter);

        studentAdapter = new StudentAdapter(this::openEditAttendanceDialog);
        rvStudents.setAdapter(studentAdapter);

        btnAdd.setOnClickListener(view -> openCreateAnnouncementDialog());

        // chỉ load khi có id
        if (!TextUtils.isEmpty(scheduleId)) {
            loadAnnouncements();
        }
        if (!TextUtils.isEmpty(classId)) {
            loadStudents();
        }

        return v;
    }

    private void loadAnnouncements() {
        ApiService.getSOService()
                .getScheduleAnnouncements(scheduleId)
                .enqueue(new Callback<ScheduleAnnouncementResponse>() {
                    @Override
                    public void onResponse(Call<ScheduleAnnouncementResponse> call,
                                           Response<ScheduleAnnouncementResponse> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            annAdapter.setData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ScheduleAnnouncementResponse> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Lỗi tải thông báo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStudents() {
        ApiService.getSOService()
                .getScheduleStudents(classId)
                .enqueue(new Callback<ScheduleStudentsResponse>() {
                    @Override
                    public void onResponse(Call<ScheduleStudentsResponse> call,
                                           Response<ScheduleStudentsResponse> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            studentAdapter.setData(response.body().getData(), scheduleId);
                        }
                    }

                    @Override
                    public void onFailure(Call<ScheduleStudentsResponse> call, Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "Lỗi tải sinh viên: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openCreateAnnouncementDialog() {
        // dùng context của fragment
        Dialog d = new Dialog(requireContext());
        d.setContentView(R.layout.dialog_create_announcement);

        EditText edtTitle = d.findViewById(R.id.edtTitle);
        EditText edtContent = d.findViewById(R.id.edtContent);
        View btnSave = d.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String t = edtTitle.getText().toString().trim();
            String c = edtContent.getText().toString().trim();
            if (TextUtils.isEmpty(t)) {
                edtTitle.setError("Nhập tiêu đề");
                return;
            }

            ApiService.getSOService()
                    .createScheduleAnnouncement(new CreateAnnouncementRequest(scheduleId, t, c))
                    .enqueue(new Callback<CreateAnnouncementResponse>() {
                        @Override
                        public void onResponse(Call<CreateAnnouncementResponse> call,
                                               Response<CreateAnnouncementResponse> response) {
                            if (!isAdded()) return;
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Đã tạo thông báo", Toast.LENGTH_SHORT).show();
                                loadAnnouncements();
                                d.dismiss();
                            } else {
                                Toast.makeText(requireContext(), "Tạo thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CreateAnnouncementResponse> call, Throwable t) {
                            if (!isAdded()) return;
                            Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        d.show();
    }

    private void openEditAttendanceDialog(ScheduleStudentsResponse.StudentInSchedule stu) {
        Dialog d = new Dialog(requireContext());
        d.setContentView(R.layout.dialog_edit_attendance);

        Spinner spStatus = d.findViewById(R.id.spStatus);
        EditText edtNote = d.findViewById(R.id.edtNote);
        View btnSave = d.findViewById(R.id.btnSave);


        String currentStatus = "Not Yet";
        if (stu != null && stu.getAttendance() != null) {
            for (ScheduleStudentsResponse.StudentInSchedule.AttendanceItem a : stu.getAttendance()) {
                if (scheduleId.equals(a.getScheduleId())) {
                    currentStatus = AppUtils.normalizeStatus(a.getStatus());
                    edtNote.setText(a.getNote());
                    break;
                }
            }
        }


        String[] statuses = {"Not Yet", "Present", "Absent", "Excused"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                statuses
        );
        spStatus.setAdapter(adapter);
        int pos = Arrays.asList(statuses).indexOf(currentStatus);
        if (pos >= 0) spStatus.setSelection(pos);
        btnSave.setOnClickListener(v -> {
            String status = spStatus.getSelectedItem().toString();
            String note = edtNote.getText().toString().trim();

            UpdateAttendanceRequest body =
                    new UpdateAttendanceRequest(scheduleId, stu.get_id(), status, note, null);

            ApiService.getSOService()
                    .updateStudentAttendance(body)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            if (!isAdded()) return;
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show();
                                loadStudents();
                                d.dismiss();
                            } else {
                                Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            if (!isAdded()) return;
                            Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        d.show();
    }

    // ================== ADAPTER THÔNG BÁO ==================
    private static class AnnAdapter extends RecyclerView.Adapter<AnnAdapter.AnnVH> {
        private List<ScheduleAnnouncementResponse.ScheduleAnnouncement> data = new ArrayList<>();

        public void setData(List<ScheduleAnnouncementResponse.ScheduleAnnouncement> d) {
            data = d != null ? d : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AnnVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_schedule_announcement, parent, false);
            return new AnnVH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AnnVH h, int i) {
            ScheduleAnnouncementResponse.ScheduleAnnouncement item = data.get(i);
            h.tvTitle.setText(item.getTitle());
            h.tvContent.setText(item.getContent());
            h.tvTime.setText(item.getCreatedAt());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class AnnVH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent, tvTime;

            public AnnVH(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvAnnTitle);
                tvContent = itemView.findViewById(R.id.tvAnnContent);
                tvTime = itemView.findViewById(R.id.tvAnnTime);
            }
        }
    }

    // ================== ADAPTER SINH VIÊN ==================
    private static class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentVH> {

        interface OnStudentClick {
            void onClick(ScheduleStudentsResponse.StudentInSchedule stu);
        }

        private final OnStudentClick listener;
        private List<ScheduleStudentsResponse.StudentInSchedule> data = new ArrayList<>();
        private String currentScheduleId;

        public StudentAdapter(OnStudentClick l) {
            this.listener = l;
        }

        public void setData(List<ScheduleStudentsResponse.StudentInSchedule> d, String scheduleId) {
            data = d != null ? d : new ArrayList<>();
            this.currentScheduleId = scheduleId;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public StudentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_teacher_class_student, parent, false);
            return new StudentVH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentVH h, int i) {
            ScheduleStudentsResponse.StudentInSchedule s = data.get(i);
            h.tvName.setText(s.getLastName() + " " + s.getFirstName());
            h.tvCode.setText(s.getStudentCode());

            String st = "Not Yet";
            if (s.getAttendance() != null && currentScheduleId != null) {
                for (ScheduleStudentsResponse.StudentInSchedule.AttendanceItem a : s.getAttendance()) {
                    if (currentScheduleId.equals(a.getScheduleId())) {
                        st = a.getStatus();
                        break;
                    }
                }
            }
            h.tvStatus.setText(st);
            String avatar = s.getStudentAvatar();
            if (avatar != null && avatar.startsWith("data:image")) {
                try {
                    String base64Image = avatar.substring(avatar.indexOf(",") + 1);
                    byte[] decoded = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                    h.imgAvatar.setImageBitmap(bmp);
                } catch (Exception e) {
                    h.imgAvatar.setImageResource(R.drawable.ic_person);
                }
            } else {
                h.imgAvatar.setImageResource(R.drawable.ic_person);
            }
            h.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(s);
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class StudentVH extends RecyclerView.ViewHolder {
            TextView tvName, tvCode, tvStatus;
            ImageView imgAvatar;

            public StudentVH(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvStudentName);
                tvCode = itemView.findViewById(R.id.tvStudentCode);
                tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);
                imgAvatar = itemView.findViewById(R.id.imgAvatar);

            }
        }
    }
}
