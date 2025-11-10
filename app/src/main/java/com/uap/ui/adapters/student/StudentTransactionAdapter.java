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
import com.uap.data.remote.response.student.TransactionHistoryResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentTransactionAdapter extends RecyclerView.Adapter<StudentTransactionAdapter.VH> {

    private final List<TransactionHistoryResponse.TransactionItem> data = new ArrayList<>();
    private final SimpleDateFormat iso =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    private final SimpleDateFormat outFmt =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void setData(List<TransactionHistoryResponse.TransactionItem> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TransactionHistoryResponse.TransactionItem t = data.get(position);
        h.tvOrderId.setText("Mã: " + t.getOrderId());

        // kỳ
        String sem = "";
        if (t.getFeeId() != null && t.getFeeId().getSemesterId() != null) {
            sem = t.getFeeId().getSemesterId().getSemesterName();
        }
        h.tvSemester.setText("Kỳ: " + (sem.isEmpty() ? "—" : sem));

        // amount
        h.tvAmount.setText(formatMoney(t.getAmount()) + " đ");

        // method
        h.tvMethod.setText("Phương thức: " + (t.getPaymentMethod() == null ? "—" : t.getPaymentMethod()));

        // time
        h.tvCreatedAt.setText(formatTime(t.getCreatedAt()));

        // status
        h.tvStatus.setText(t.getStatus());
        int color = R.color.orange;
        if ("Success".equalsIgnoreCase(t.getStatus())) {
            color = R.color.teal_200;
        } else if ("Failed".equalsIgnoreCase(t.getStatus())) {
            color = android.R.color.holo_red_dark;
        }
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
        TextView tvOrderId, tvStatus, tvSemester, tvAmount, tvMethod, tvCreatedAt;
        VH(@NonNull View v) {
            super(v);
            tvOrderId = v.findViewById(R.id.tvOrderId);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvSemester = v.findViewById(R.id.tvSemester);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvMethod = v.findViewById(R.id.tvMethod);
            tvCreatedAt = v.findViewById(R.id.tvCreatedAt);
        }
    }

    private String formatMoney(long amount) {
        return String.format(Locale.getDefault(), "%,d", amount).replace(',', '.');
    }

    private String formatTime(String isoStr) {
        if (isoStr == null) return "";
        try {
            Date d = iso.parse(isoStr);
            return outFmt.format(d);
        } catch (ParseException e) {
            return isoStr;
        }
    }
}
