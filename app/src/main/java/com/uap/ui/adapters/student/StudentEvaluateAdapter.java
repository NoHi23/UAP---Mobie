package com.uap.ui.adapters.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.data.remote.response.student.StudentClassListResponse;

import java.util.ArrayList;
import java.util.List;

public class StudentEvaluateAdapter extends RecyclerView.Adapter<StudentEvaluateAdapter.VH> {

    public interface OnEvaluateClick {
        void onEvaluate(StudentClassListResponse.StudentClassItem item);
    }

    private final List<StudentClassListResponse.StudentClassItem> data = new ArrayList<>();
    private final OnEvaluateClick listener;

    public StudentEvaluateAdapter(OnEvaluateClick l) {
        this.listener = l;
    }

    public void setData(List<StudentClassListResponse.StudentClassItem> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_evaluate, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        StudentClassListResponse.StudentClassItem item = data.get(position);
        h.tvSubjectName.setText(item.getSubjectId() != null
                ? item.getSubjectId().getSubjectName()
                : "(Không rõ môn)");
        h.tvClassName.setText(item.getClassName());

        if (item.getLecturerId() != null) {
            h.tvLecturer.setText("GV: " + item.getLecturerId().getFullName().trim());
        } else {
            h.tvLecturer.setText("GV: —");
        }

        h.btnEvaluate.setOnClickListener(v -> {
            if (listener != null) listener.onEvaluate(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvClassName, tvLecturer;
        Button btnEvaluate;
        VH(@NonNull View v) {
            super(v);
            tvSubjectName = v.findViewById(R.id.tvSubjectName);
            tvClassName = v.findViewById(R.id.tvClassName);
            tvLecturer = v.findViewById(R.id.tvLecturer);
            btnEvaluate = v.findViewById(R.id.btnEvaluate);
        }
    }
}
