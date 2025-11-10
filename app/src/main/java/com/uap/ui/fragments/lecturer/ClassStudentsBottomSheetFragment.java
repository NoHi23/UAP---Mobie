package com.uap.ui.fragments.lecturer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.uap.App;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.lecturer.ScheduleStudentsResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassStudentsBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_CLASS_ID = "class_id";
    private String classId;

    public static ClassStudentsBottomSheetFragment newInstance(String classId, String className) {
        ClassStudentsBottomSheetFragment s = new ClassStudentsBottomSheetFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CLASS_ID, classId);
        s.setArguments(b);
        return s;
    }

    private RecyclerView recyclerView;
    private StudentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_class_students, container, false);
        classId = getArguments() != null ? getArguments().getString(ARG_CLASS_ID) : null;
        recyclerView = v.findViewById(R.id.rvStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        adapter = new StudentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadStudents();
        return v;
    }

    private void loadStudents() {
        ApiService.getSOService().getScheduleStudents(classId)
                .enqueue(new Callback<ScheduleStudentsResponse>() {
                    @Override
                    public void onResponse(Call<ScheduleStudentsResponse> call,
                                           Response<ScheduleStudentsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setData(response.body().getData());
                        } else {
                            Toast.makeText(App.getContext(), "Không có sinh viên", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ScheduleStudentsResponse> call, Throwable t) {
                        Toast.makeText(App.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ------------------ StudentAdapter ------------------

    private static class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentVH> {

        private List<ScheduleStudentsResponse.StudentInSchedule> data;

        public StudentAdapter(List<ScheduleStudentsResponse.StudentInSchedule> data) {
            this.data = data;
        }

        public void setData(List<ScheduleStudentsResponse.StudentInSchedule> newData) {
            this.data = newData != null ? newData : new ArrayList<>();
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
            var s = data.get(i);
            h.tvName.setText(s.getLastName() + " " + s.getFirstName());
            h.tvCode.setText(s.getStudentCode());

//            // Nếu có trạng thái điểm danh
//            String st = "Not Yet";
//            if (s.getAttendance() != null && !s.getAttendance().isEmpty()) {
//                st = s.getAttendance().get(0).getStatus();
//            }
//            h.tvStatus.setText("Trạng thái: " + st);
            String avatar = s.getStudentAvatar(); // lấy trường avatar từ API
            if (avatar != null && avatar.startsWith("data:image")) {
                try {
                    String base64Image = avatar.substring(avatar.indexOf(",") + 1);
                    byte[] decoded = Base64.decode(base64Image, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                    h.imgAvatar.setImageBitmap(bmp);
                } catch (Exception e) {
                    h.imgAvatar.setImageResource(R.drawable.ic_person); // fallback nếu lỗi
                }
            } else {
                h.imgAvatar.setImageResource(R.drawable.ic_person);
            }

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
