package com.uap.ui.adapters.student;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.TimetableItem;
import com.uap.ui.dialogs.student.StudentSlotDetailDialog;

import java.util.List;

public class StudentScheduleAdapter extends RecyclerView.Adapter<StudentScheduleAdapter.VH> {

    private final List<TimetableItem> data;

    public StudentScheduleAdapter(List<TimetableItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_timetable, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TimetableItem item = data.get(position);

        String subjectName = "";
        if (item.getSubjectId() != null) {
            subjectName = item.getSubjectId().getSubjectName();
        }
        h.tvCourseName.setText(subjectName);

        String teacher = "";
        if (item.getLecturerId() != null) {
            teacher = item.getLecturerId().getLastName() + " " + item.getLecturerId().getFirstName();
        }
        h.tvLecturer.setText("GV: " + teacher);

        String room = (item.getRoomId() != null) ? item.getRoomId().getRoomName() : "Chưa có phòng";
        h.tvRoom.setText(room);
        switch (item.getAttendanceStatus()) {
            case "Present":
                h.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // xanh lá
                break;
            case "Absent":
                h.tvStatus.setTextColor(Color.parseColor("#F44336")); // đỏ
                break;
            default:
                h.tvStatus.setTextColor(Color.parseColor("#2196F3")); // xanh dương
                break;
        }

        h.tvStatus.setText("Trạng thái: " + item.getAttendanceStatus());

        h.tvStartPeriod.setText(String.valueOf(item.getSlot()));
        h.tvEndPeriod.setText(String.valueOf(item.getSlot() + 1));
        h.itemView.setOnClickListener(v -> {
            Context ctx = v.getContext();
            StudentSlotDetailDialog dialog = new StudentSlotDetailDialog(ctx, item);
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvStartPeriod, tvEndPeriod, tvCourseName, tvLecturer, tvRoom, tvStatus;

        VH(@NonNull View v) {
            super(v);
            tvStartPeriod = v.findViewById(R.id.tvStartPeriod);
            tvEndPeriod = v.findViewById(R.id.tvEndPeriod);
            tvCourseName = v.findViewById(R.id.tvCourseName);
            tvLecturer = v.findViewById(R.id.tvLecturer);
            tvRoom = v.findViewById(R.id.tvRoom);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}
