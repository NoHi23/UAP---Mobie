package com.uap.ui.adapters.lecturer;


import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uap.R;
import com.uap.data.remote.response.lecturer.SupportRequestListResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SupportRequestAdapter extends RecyclerView.Adapter<SupportRequestAdapter.VH> {

    private final List<SupportRequestListResponse.SupportRequest> data = new ArrayList<>();
    private final SimpleDateFormat in =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private final SimpleDateFormat out =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void setData(List<SupportRequestListResponse.SupportRequest> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_support_request, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        SupportRequestListResponse.SupportRequest item = data.get(i);

        // request ở dạng HTML -> render gọn
        if (item.getRequest() != null) {
            h.tvRequestContent.setText(Html.fromHtml(item.getRequest()));
        } else {
            h.tvRequestContent.setText("(Không có nội dung)");
        }

        h.tvStatus.setText(item.getStatus());

        String created = item.getCreatedAt();
        if (created != null) {
            try {
                h.tvCreatedAt.setText(out.format(in.parse(created)));
            } catch (Exception e) {
                h.tvCreatedAt.setText(created);
            }
        } else {
            h.tvCreatedAt.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRequestContent, tvStatus, tvCreatedAt;
        VH(@NonNull View itemView) {
            super(itemView);
            tvRequestContent = itemView.findViewById(R.id.tvRequestContent);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
