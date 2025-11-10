package com.uap.ui.adapters.lecturer;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uap.R;
import com.uap.domain.model.LecturerScheduleItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LecturerScheduleAdapter extends RecyclerView.Adapter<LecturerScheduleAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(LecturerScheduleItem item);
    }
    private OnItemClickListener listener;

    private final List<LecturerScheduleItem> data;
    private final SimpleDateFormat iso =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final SimpleDateFormat out =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public LecturerScheduleAdapter(List<LecturerScheduleItem> data) {
        this.data = data;
    }
    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lecturer_schedule, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        LecturerScheduleItem item = data.get(i);

        h.tvCourseName.setText(item.getSubjectId() != null
                ? item.getSubjectId().getSubjectName()
                : "Môn học");

        h.tvClass.setText("Lớp: " + (item.getClassId() != null ? item.getClassId().getClassName() : ""));
        h.tvRoom.setText(item.getRoomId() != null ? item.getRoomId().getRoomName() : "");
        h.tvSlot.setText("Tiết " + item.getSlot());
        h.tvTime.setText(item.getStartTime() + " - " + item.getEndTime());

        String dateStr = item.getDate();
        if (dateStr != null) {
            try {
                h.tvDate.setText("Ngày: " + out.format(iso.parse(dateStr)));
            } catch (Exception e) {
                h.tvDate.setText("Ngày: " + dateStr);
            }
        }

        // trạng thái dạy / điểm danh
        if (item.getLectureAttendance() != null && item.getLectureAttendance().isAttendance()) {
            h.tvStatus.setText("Đã điểm danh");
            h.tvStatus.setTextColor(0xFF1B5E20); // xanh
        } else {
            h.tvStatus.setText("Chưa điểm danh");
            h.tvStatus.setTextColor(0xFFD32F2F); // đỏ
        }
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSlot, tvTime, tvCourseName, tvClass, tvRoom, tvDate, tvStatus;
        VH(@NonNull View v) {
            super(v);
            tvSlot = v.findViewById(R.id.tvSlot);
            tvTime = v.findViewById(R.id.tvTime);
            tvCourseName = v.findViewById(R.id.tvCourseName);
            tvClass = v.findViewById(R.id.tvClass);
            tvRoom = v.findViewById(R.id.tvRoom);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}
