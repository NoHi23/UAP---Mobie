package com.uap.ui.adapters.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.NotificationItem;

import java.util.List;


public class StudentNotificationAdapter extends RecyclerView.Adapter<StudentNotificationAdapter.VH> {
    private final List<NotificationItem> data;

    public StudentNotificationAdapter(List<NotificationItem> d) {
        data = d;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_slot_notification, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        NotificationItem n = data.get(i);
        h.tvTitle.setText(n.getTitle());
        h.tvContent.setText(n.getContent());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;

        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvContent = v.findViewById(R.id.tvContent);
        }
    }
}

