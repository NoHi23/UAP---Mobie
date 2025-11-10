package com.uap.ui.adapters.student;


import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.data.remote.response.student.AnnouncementResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentAnnouncementAdapter extends RecyclerView.Adapter<StudentAnnouncementAdapter.VH> {

    public interface OnAnnouncementClick {
        void onClick(AnnouncementResponse.Announcement item);
    }

    private final List<AnnouncementResponse.Announcement> data = new ArrayList<>();
    private final OnAnnouncementClick listener;
    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private final SimpleDateFormat outFmt = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());

    public StudentAnnouncementAdapter(OnAnnouncementClick l) {
        this.listener = l;
    }

    public void setData(List<AnnouncementResponse.Announcement> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_announcement, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        AnnouncementResponse.Announcement a = data.get(position);
        h.tvTitle.setText(a.getTitle());

        // Chuyển content HTML sang text ngắn
        Spanned sp = Html.fromHtml(a.getContent() != null ? a.getContent() : "", Html.FROM_HTML_MODE_LEGACY);
        h.tvContent.setText(sp);

        // Định dạng ngày
        String timeStr = "";
        try {
            Date d = iso.parse(a.getCreatedAt());
            timeStr = outFmt.format(d);
        } catch (Exception ignored) {}

        h.tvTimestamp.setText(timeStr);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(a);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvTimestamp;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.textViewTitle);
            tvContent = v.findViewById(R.id.textViewContent);
            tvTimestamp = v.findViewById(R.id.textViewTimestamp);
        }
    }
}
