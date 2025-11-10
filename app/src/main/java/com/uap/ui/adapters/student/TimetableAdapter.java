package com.uap.ui.adapters.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.TimetableItem;

import java.util.List;

// Giả định TimetableItem.java đã được định nghĩa
// Giả định layout item_today_schedule.xml đã được định nghĩa

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private List<TimetableItem> scheduleList;

    public TimetableAdapter(List<TimetableItem> scheduleList) {
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                // Inflate layout item_today_schedule.xml đã tạo ở trên
                .inflate(R.layout.item_today_schedule, parent, false);
        return new TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        TimetableItem item = scheduleList.get(position);

        // Hiển thị thời gian (Ghép start và end time)
        String timeRange = String.format("%s - %s", item.getStartTime(), item.getEndTime());
        holder.tvTimeRange.setText(timeRange);

        // Tên môn học
        holder.tvSubjectName.setText(item.getSubjectId().getSubjectName());

        // Mã lớp
        holder.tvClassCode.setText(String.format("Lớp: %s", item.getClassId().getClassName()));

        // Phòng học
        holder.tvRoom.setText(String.format("Phòng: %s", item.getRoomId().getRoomName()));

        // Tên giảng viên (Có thể bỏ qua nếu đã hiển thị ở màn hình chính)
        holder.tvLecturerName.setText(String.format("GV: %s", item.getLecturerId().getFirstName()));

        // Xử lý sự kiện click trên item (ví dụ: chuyển sang màn hình điểm danh)
        holder.itemView.setOnClickListener(v -> {
            // TODO: Xử lý click item, ví dụ: start new activity for attendance
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    // ViewHolder: Giữ tham chiếu đến các view trong layout item
    static class TimetableViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeRange;
        TextView tvSubjectName;
        TextView tvClassCode;
        TextView tvRoom;
        TextView tvLecturerName;

        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeRange = itemView.findViewById(R.id.tvTimeRange);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvClassCode = itemView.findViewById(R.id.tvClassCode);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvLecturerName = itemView.findViewById(R.id.tvLecturerName);
        }
    }
}