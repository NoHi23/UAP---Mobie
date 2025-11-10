package com.uap.ui.adapters.lecturer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uap.R;
import com.uap.data.remote.response.lecturer.LecturerClassesResponse;
import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassVH> {

    public interface OnClassClickListener {
        void onClick(LecturerClassesResponse.ClassInfo cls);
    }

    private List<LecturerClassesResponse.ClassInfo> data;
    private final OnClassClickListener listener;

    public ClassesAdapter(List<LecturerClassesResponse.ClassInfo> data, OnClassClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<LecturerClassesResponse.ClassInfo> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lecturer_class, parent, false);
        return new ClassVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassVH holder, int position) {
        var cls = data.get(position);
        holder.tvClassName.setText(cls.getClassName());
        holder.tvSubject.setText(cls.getSubjectName() + " (" + cls.getSubjectCode() + ")");
        holder.tvStudents.setText("Số SV: " + cls.getStudentCount());

        if (cls.getSchedules() != null && !cls.getSchedules().isEmpty()) {
            var s = cls.getSchedules().get(0);
            holder.tvSchedule.setText("Bắt đầu: " + s.getDate().substring(0, 10) + " - " + s.getRoom());
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(cls));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ClassVH extends RecyclerView.ViewHolder {
        TextView tvClassName, tvSubject, tvStudents, tvSchedule;
        ClassVH(@NonNull View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvStudents = itemView.findViewById(R.id.tvStudents);
            tvSchedule = itemView.findViewById(R.id.tvSchedule);
        }
    }
}
