package com.uap.ui.fragments.student;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.uap.App;
import com.uap.R;
import com.uap.data.local.AuthManager;
import com.uap.data.remote.ApiService;

import com.uap.data.remote.request.UpdateProfileRequest;
import com.uap.data.remote.response.UpdateProfileResponse;
import com.uap.domain.model.StudentProfile;
import com.uap.ui.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentProfileFragment extends Fragment {

    private ShapeableImageView imgAvatar;
    private TextView tvName;
    private TextView tvStudentCode;

    private TextView tvEmailSchool, tvPhone, tvGender, tvDob, tvMajor, tvAddress;

    private ProgressBar progress;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View contentContainer;
    private TextView btnEditProfile , btnLogout;

    private StudentProfile currentProfile;

    private String selectedAvatarBase64;

    private ActivityResultLauncher<String> pickImageLauncher;
    private ImageView dialogImgPreview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && dialogImgPreview != null) {
                        dialogImgPreview.setImageURI(uri);
                        selectedAvatarBase64 = encodeImageToDataUri(uri);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_profile, container, false);

        contentContainer = v.findViewById(R.id.contentContainer);
        progress = v.findViewById(R.id.progress);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);

        imgAvatar = v.findViewById(R.id.imgAvatar);
        tvName = v.findViewById(R.id.tvName);
        tvStudentCode = v.findViewById(R.id.tvStudentCode);

        View rowEmail = v.findViewById(R.id.row_email_school);
        tvEmailSchool = rowEmail.findViewById(R.id.tvValue);

        View rowPhone = v.findViewById(R.id.row_phone);
        tvPhone = rowPhone.findViewById(R.id.tvValue);

        View rowGender = v.findViewById(R.id.row_gender);
        tvGender = rowGender.findViewById(R.id.tvValue);

        View rowDob = v.findViewById(R.id.row_dob);
        tvDob = rowDob.findViewById(R.id.tvValue);

        View rowMajor = v.findViewById(R.id.row_major);
        tvMajor = rowMajor.findViewById(R.id.tvValue);

        View rowAddress = v.findViewById(R.id.row_address);
        tvAddress = rowAddress.findViewById(R.id.tvValue);

        ((TextView) rowEmail.findViewById(R.id.tvLabel)).setText("Email trường");
        ((TextView) rowPhone.findViewById(R.id.tvLabel)).setText("Số điện thoại");
        ((TextView) rowGender.findViewById(R.id.tvLabel)).setText("Giới tính");
        ((TextView) rowDob.findViewById(R.id.tvLabel)).setText("Ngày sinh");
        ((TextView) rowMajor.findViewById(R.id.tvLabel)).setText("Chuyên ngành");
        ((TextView) rowAddress.findViewById(R.id.tvLabel)).setText("Địa chỉ");

        btnEditProfile = v.findViewById(R.id.btnEditProfile);
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(view -> openEditDialog());
        }
        btnLogout = v.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(view -> logout());
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::loadProfile);
        }

        loadProfile();

        return v;
    }
    private void logout(){
        AuthManager.logout(App.getContext());

        // Mở màn hình login
        Intent intent = new Intent(App.getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        App.getContext().startActivity(intent);
    }
    private void loadProfile() {
        showLoading(true);
        ApiService.getSOService()
                .getStudentProfile()
                .enqueue(new Callback<StudentProfile>() {
                    @Override
                    public void onResponse(Call<StudentProfile> call, Response<StudentProfile> response) {
                        if (!isAdded()) {
                            showLoading(false);
                            return;
                        }
                        if (response.isSuccessful() && response.body() != null) {
                            currentProfile = response.body();
                            bindData(response.body());
                        }
                        showLoading(false);
                    }

                    @Override
                    public void onFailure(Call<StudentProfile> call, Throwable t) {
                        if (!isAdded()) return;
                        showLoading(false);
                        Toast.makeText(getContext(), "Không tải được hồ sơ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading(boolean isLoading) {
        if (progress != null) progress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (contentContainer != null) contentContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing() && !isLoading) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void bindData(@NonNull StudentProfile s) {
        String fullName = safe(s.getLastName()) + " " + safe(s.getFirstName());
        tvName.setText(fullName.trim());
        tvStudentCode.setText("Mã SV: " + safe(s.getStudentCode()));

        tvEmailSchool.setText(safe(s.getEmail()));
        tvPhone.setText(safe(s.getPhone()));
        tvGender.setText(s.isGender() ? "Nam" : "Nữ");
        tvDob.setText(formatDate(s.getDateOfBirth()));
        if (s.getMajorId() != null) {
            tvMajor.setText(s.getMajorId().getMajorName());
        } else {
            tvMajor.setText("—");
        }
        tvAddress.setText(safe(s.getAddress()));

        loadAvatar(s.getStudentAvatar());
    }

    private void loadAvatar(String avatar) {
        if (avatar == null || avatar.isEmpty()) {
            imgAvatar.setImageResource(R.drawable.ic_person);
            return;
        }

        if (avatar.startsWith("data:image")) {
            int comma = avatar.indexOf(",");
            if (comma != -1) {
                avatar = avatar.substring(comma + 1);
            }
        }

        try {
            byte[] decoded = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            if (bmp != null) {
                imgAvatar.setImageBitmap(bmp);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_person);
            }
        } catch (Exception e) {
            imgAvatar.setImageResource(R.drawable.ic_person);
        }
    }

    private void openEditDialog() {
        if (getContext() == null) return;

        selectedAvatarBase64 = null;
        dialogImgPreview = null;

        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.dialog_student_update_profile);

        EditText edtPhone = d.findViewById(R.id.edtPhone);
        EditText edtAddress = d.findViewById(R.id.edtAddress);
        AutoCompleteTextView edtGender = d.findViewById(R.id.edtGender);
        ImageView imgPreview = d.findViewById(R.id.imgPreview);
        TextView btnChooseImage = d.findViewById(R.id.btnChooseImage);
        View btnSave = d.findViewById(R.id.btnSave);

        dialogImgPreview = imgPreview;

        String[] genders = new String[]{"Nam", "Nữ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        edtGender.setAdapter(adapter);

        if (currentProfile != null) {
            edtPhone.setText(safe(currentProfile.getPhone()));
            edtAddress.setText(safe(currentProfile.getAddress()));
            edtGender.setText(currentProfile.isGender() ? "Nam" : "Nữ", false);

            if (currentProfile.getStudentAvatar() != null && !currentProfile.getStudentAvatar().isEmpty()) {
                loadAvatarIntoImageView(currentProfile.getStudentAvatar(), imgPreview);
            }
        }

        View.OnClickListener pickImg = v -> pickImageLauncher.launch("image/*");
        btnChooseImage.setOnClickListener(pickImg);
        imgPreview.setOnClickListener(pickImg);

        btnSave.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String genderStr = edtGender.getText().toString().trim();

            Boolean gender = null;
            if (genderStr.equalsIgnoreCase("nam")) gender = true;
            else if (genderStr.equalsIgnoreCase("nữ")) gender = false;

            doUpdateProfile(phone, address, gender, selectedAvatarBase64, d);
        });

        if (d.getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            d.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        d.show();
    }


    private void doUpdateProfile(String phone,
                                 String address,
                                 @Nullable Boolean gender,
                                 @Nullable String avatarBase64,
                                 Dialog dialog) {
        showLoading(true);

        UpdateProfileRequest body = new UpdateProfileRequest(phone, address, gender, avatarBase64);

        ApiService.getSOService()
                .updateStudentProfile(body)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call,
                                           Response<UpdateProfileResponse> response) {
                        showLoading(false);
                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            UpdateProfileResponse res = response.body();
//                            if (res.getStudent() != null) {
//                                currentProfile = res.getStudent();
//                                bindData(res.getStudent());
//                            }
                            loadProfile();
                            Toast.makeText(getContext(),
                                    res.getMessage() != null ? res.getMessage() : "Cập nhật thành công",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        showLoading(false);
                        if (!isAdded()) return;
                        Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String encodeImageToDataUri(Uri uri) {
        if (getContext() == null) return null;
        try {
            InputStream is = getContext().getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] bytes = baos.toByteArray();
            String b64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
            return "data:image/jpeg;base64," + b64;
        } catch (Exception e) {
            return null;
        }
    }

    private void loadAvatarIntoImageView(String avatar, ImageView img) {
        if (avatar == null || avatar.isEmpty()) return;
        if (avatar.startsWith("data:image")) {
            int comma = avatar.indexOf(",");
            if (comma != -1) {
                avatar = avatar.substring(comma + 1);
            }
        }
        try {
            byte[] decoded = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            img.setImageBitmap(bmp);
        } catch (Exception ignored) {}
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String formatDate(String iso) {
        if (iso == null || iso.length() < 10) return "—";
        return iso.substring(0, 10);
    }
}
