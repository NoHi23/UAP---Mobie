package com.uap.ui.adapters.student;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.ExamItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StudentExamAdapter extends RecyclerView.Adapter<StudentExamAdapter.VH> {

    private final List<ExamItem> exams;

    public StudentExamAdapter(List<ExamItem> exams) {
        this.exams = exams;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_exam, parent, false);
        return new VH(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ExamItem item = exams.get(position);
        if (item.getExamSchedule() == null) return;

        // Format ngày đẹp hơn
        String date = item.getExamSchedule().getExamDate();
        String formattedDate = date;
        try {
            java.util.Date d = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(date);
            formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(d);
        } catch (Exception ignored) {}

        h.tvExamCourseName.setText(item.getExamSchedule().getCourseName());
        h.tvExamDate.setText("Ngày thi: " + formattedDate);
        h.tvExamTime.setText("Giờ thi: " + item.getExamSchedule().getTime());
        h.tvExamLocation.setText("Phòng thi: " + item.getExamSchedule().getRoom());
        h.tvExamSeatNumber.setText("Ghi chú: " + item.getExamSchedule().getNote());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvExamCourseName, tvExamDate, tvExamTime, tvExamLocation, tvExamSeatNumber;
        VH(@NonNull View v) {
            super(v);
            tvExamCourseName = v.findViewById(R.id.tvExamCourseName);
            tvExamDate = v.findViewById(R.id.tvExamDate);
            tvExamTime = v.findViewById(R.id.tvExamTime);
            tvExamLocation = v.findViewById(R.id.tvExamLocation);
            tvExamSeatNumber = v.findViewById(R.id.tvExamSeatNumber);
        }
    }
}
