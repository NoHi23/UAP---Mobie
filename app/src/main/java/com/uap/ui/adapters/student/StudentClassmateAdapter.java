package com.uap.ui.adapters.student;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.Classmate;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentClassmateAdapter extends RecyclerView.Adapter<StudentClassmateAdapter.VH> {

    private final List<Classmate> data;
    private final ExecutorService executor = Executors.newFixedThreadPool(2); // decode ảnh nhẹ nhàng hơn

    public StudentClassmateAdapter(List<Classmate> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_classmate, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Classmate c = data.get(position);
        holder.tvName.setText(c.getLastName() + " " + c.getFirstName());
        holder.tvCode.setText(c.getStudentCode());

        //holder.imgAvatar.setImageResource(R.drawable.ic_person);
        String avatarBase64 = c.getStudentAvatar();
        if (avatarBase64 != null && !avatarBase64.isEmpty()) {
            // Nếu có tiền tố "data:image/jpeg;base64,"
            if (avatarBase64.startsWith("data:image")) {
                int commaIndex = avatarBase64.indexOf(",");
                if (commaIndex != -1) {
                    avatarBase64 = avatarBase64.substring(commaIndex + 1);
                }
            }

            try {
                byte[] decoded = Base64.decode(avatarBase64, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                holder.imgAvatar.setImageBitmap(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_person);
        }

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView imgAvatar;
        final TextView tvName;
        final TextView tvCode;

        VH(@NonNull View v) {
            super(v);
            imgAvatar = v.findViewById(R.id.imgAvatar);
            tvName = v.findViewById(R.id.tvName);
            tvCode = v.findViewById(R.id.tvCode);
        }
    }
}
