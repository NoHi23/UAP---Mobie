package com.uap.ui.adapters.student;


import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.data.remote.response.student.RequestResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentRequestAdapter extends RecyclerView.Adapter<StudentRequestAdapter.VH> {

    private final List<RequestResponse.StudentRequest> data = new ArrayList<>();
    private final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private final SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void setData(List<RequestResponse.StudentRequest> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_request, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        RequestResponse.StudentRequest r = data.get(position);
        h.tvTitle.setText(r.getTitle());
        h.tvRequestType.setText("Loại: " + r.getRequestType());
        h.tvDescription.setText(r.getDescription());

        String formattedDate = "";
        try {
            Date d = iso.parse(r.getCreatedAt());
            formattedDate = out.format(d);
        } catch (ParseException ignored) {}
        h.tvDate.setText(formattedDate);

        h.tvStatus.setText(r.getStatus());
        int color = R.color.orange;
        if ("Approved".equalsIgnoreCase(r.getStatus())) color = R.color.teal_200;
        else if ("Rejected".equalsIgnoreCase(r.getStatus())) color = android.R.color.holo_red_dark;
        h.tvStatus.getBackground().setColorFilter(
                ContextCompat.getColor(h.itemView.getContext(), color),
                PorterDuff.Mode.SRC_IN
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvRequestType, tvDescription, tvDate, tvStatus;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvRequestType = v.findViewById(R.id.tvRequestType);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}
