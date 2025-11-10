package com.uap.ui.adapters.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.data.remote.response.student.StudentAttendanceResponse;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.VH> {

    private final List<StudentAttendanceResponse.AttendanceSubject> items = new ArrayList<>();

    public void setData(List<StudentAttendanceResponse.AttendanceSubject> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        StudentAttendanceResponse.AttendanceSubject s = items.get(position);
        h.tvSubjectName.setText(s.getSubjectName());
        h.tvClassName.setText(s.getClassName());

        h.tvRate.setText("Điểm danh: " + s.getAttendanceRate() + "%");
        h.tvSlotInfo.setText(s.getAbsentSlots() + " / " + s.getTotalSlots() + " buổi vắng");

        if (s.isFailed()) {
            h.tvStatus.setText("Cảnh báo");
            h.tvStatus.setBackgroundResource(R.drawable.bg_attendance_warning);
        } else {
            h.tvStatus.setText("Đạt");
            h.tvStatus.setBackgroundResource(R.drawable.bg_attendance_ok);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvClassName, tvStatus, tvRate, tvSlotInfo;

        VH(@NonNull View v) {
            super(v);
            tvSubjectName = v.findViewById(R.id.tvSubjectName);
            tvClassName = v.findViewById(R.id.tvClassName);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvRate = v.findViewById(R.id.tvRate);
            tvSlotInfo = v.findViewById(R.id.tvSlotInfo);
        }
    }
}
