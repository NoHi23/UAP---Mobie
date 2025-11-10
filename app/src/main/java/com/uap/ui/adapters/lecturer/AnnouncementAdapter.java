package com.uap.ui.adapters.lecturer;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.data.remote.response.lecturer.ScheduleAnnouncementResponse;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.VH> {

    private List<ScheduleAnnouncementResponse.ScheduleAnnouncement> data = new ArrayList<>();

    public void setData(List<ScheduleAnnouncementResponse.ScheduleAnnouncement> d) {
        this.data = d != null ? d : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_announcement, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        var a = data.get(i);
        h.tvTitle.setText(a.getTitle());
        h.tvContent.setText(a.getContent());
        h.tvTime.setText(a.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvTime;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvAnnTitle);
            tvContent = v.findViewById(R.id.tvAnnContent);
            tvTime = v.findViewById(R.id.tvAnnTime);
        }
    }
}