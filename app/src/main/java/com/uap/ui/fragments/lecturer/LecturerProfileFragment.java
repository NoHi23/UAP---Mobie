package com.uap.ui.fragments.lecturer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.uap.App;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.lecturer.UpdateLecturerProfileRequest;
import com.uap.data.remote.response.BasicResponse;
import com.uap.data.remote.response.lecturer.LecturerProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerProfileFragment extends Fragment {

    private TextView tvLecturerName, tvLecturerCode, tvEmail, tvMajor;
    private EditText edtAddress, edtPhone, edtDob;
    private Spinner spGender;
    private MaterialButton btnSave;

    private String lecturerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lecturer_profile, container, false);
        tvLecturerName = v.findViewById(R.id.tvLecturerName);
        tvLecturerCode = v.findViewById(R.id.tvLecturerCode);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvMajor = v.findViewById(R.id.tvMajor);
        edtAddress = v.findViewById(R.id.edtAddress);
        edtPhone = v.findViewById(R.id.edtPhone);
        edtDob = v.findViewById(R.id.edtDob);
        spGender = v.findViewById(R.id.spGender);
        btnSave = v.findViewById(R.id.btnSave);

        setupGenderSpinner();
        loadLecturerInfo();

        btnSave.setOnClickListener(view -> updateLecturerInfo());
        return v;
    }

    private void setupGenderSpinner() {
        String[] genders = {"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, genders);
        spGender.setAdapter(adapter);
    }

    private void loadLecturerInfo() {
        ApiService.getSOService()
                .getLecturerProfile()
                .enqueue(new Callback<LecturerProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LecturerProfileResponse> call,
                                           @NonNull Response<LecturerProfileResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            var d = response.body().getData();
                            lecturerId = d.get_id();
                            tvLecturerName.setText(d.getLastName() + " " + d.getFirstName());
                            tvLecturerCode.setText("Mã GV: " + d.getLecturerCode());
                            tvEmail.setText("Email: " + d.getAccount().getEmail());
                            tvMajor.setText("Ngành: " + d.getMajorId().getMajorName());
                            edtAddress.setText(d.getAddress());
                            edtPhone.setText(d.getPhone());
                            edtDob.setText(d.getDateOfBirth().substring(0, 10));
                            spGender.setSelection(d.isGender() ? 0 : 1);
                        } else {
                            Toast.makeText(App.getContext(), "Không tải được thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LecturerProfileResponse> call, @NonNull Throwable t) {
                        Toast.makeText(App.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLecturerInfo() {
        String address = edtAddress.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        boolean gender = spGender.getSelectedItemPosition() == 0;

        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(dob)) {
            Toast.makeText(App.getContext(), "Nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateLecturerProfileRequest req = new UpdateLecturerProfileRequest(address, phone, dob, gender);

        ApiService.getSOService()
                .updateLecturerProfile(req)
                .enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BasicResponse> call,
                                           @NonNull Response<BasicResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(App.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(App.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BasicResponse> call, @NonNull Throwable t) {
                        Toast.makeText(App.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
