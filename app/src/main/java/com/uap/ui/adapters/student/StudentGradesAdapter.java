package com.uap.ui.adapters.student;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.ComponentMini;
import com.uap.domain.model.GradeItem;
import com.uap.domain.model.SubjectGrade;

import java.util.List;

public class StudentGradesAdapter extends RecyclerView.Adapter<StudentGradesAdapter.VH> {

    private final List<SubjectGrade> data;
    private final LayoutInflater inflater;

    public StudentGradesAdapter(Context ctx, List<SubjectGrade> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_student_grade, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        SubjectGrade sg = data.get(position);
        h.tvCourseName.setText(sg.getSubjectName() + " (" + sg.getSubjectCode() + ")");
        h.tvFinalGrade.setText(String.format("%.1f", sg.getFinalScore()));
        h.tvTotalScore.setText("Tổng điểm (theo %): " + String.format("%.1f", sg.getFinalScore()));

        // clear components
        h.containerComponents.removeAllViews();

        for (GradeItem item : sg.getComponents()) {
            View row = inflater.inflate(R.layout.row_grade_component, h.containerComponents, false);
            TextView tvComp = row.findViewById(R.id.tvCompName);
            TextView tvScore = row.findViewById(R.id.tvCompScore);

            ComponentMini c = item.getComponentId();
            String compName = c != null ? c.getName() : "Thành phần";
            double weight = c != null ? c.getWeightPercentage() : 0;

            tvComp.setText(compName + " (" + weight + "%)");
            tvScore.setText(String.valueOf(item.getScore()));

            h.containerComponents.addView(row);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvFinalGrade, tvTotalScore;
        LinearLayout containerComponents;

        VH(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvFinalGrade = itemView.findViewById(R.id.tvFinalGrade);
            tvTotalScore = itemView.findViewById(R.id.tvTotalScore);
            containerComponents = itemView.findViewById(R.id.containerComponents);
        }
    }
}