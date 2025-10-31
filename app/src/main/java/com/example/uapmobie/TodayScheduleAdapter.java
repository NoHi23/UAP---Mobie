package com.example.uapmobie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodayScheduleAdapter extends RecyclerView.Adapter<TodayScheduleAdapter.ViewHolder> {

    private Context context;
    private List<ClassSlot> classSlotList;

    public TodayScheduleAdapter(Context context, List<ClassSlot> classSlotList) {
        this.context = context;
        this.classSlotList = classSlotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_class_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassSlot classSlot = classSlotList.get(position);
        holder.tvCourseName.setText(classSlot.getCourseName());
        holder.tvLecturer.setText(classSlot.getLecturerName());
        holder.tvRoom.setText(classSlot.getRoom() + " - " + classSlot.getBuilding());
        holder.tvStartPeriod.setText(String.valueOf(classSlot.getStartPeriod()));
        holder.tvEndPeriod.setText(String.valueOf(classSlot.getEndPeriod()));
    }

    @Override
    public int getItemCount() {
        return classSlotList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvLecturer, tvRoom, tvStartPeriod, tvEndPeriod;
        // Thêm các TextView khác nếu cần

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvStartPeriod = itemView.findViewById(R.id.tvStartPeriod);
            tvEndPeriod = itemView.findViewById(R.id.tvEndPeriod);
            tvLecturer = itemView.findViewById(R.id.tvLecturer);
            tvRoom = itemView.findViewById(R.id.tvRoom);
        }
    }
}