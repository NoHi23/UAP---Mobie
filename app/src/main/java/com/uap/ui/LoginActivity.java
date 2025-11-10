package com.uap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.uap.R;
import com.uap.data.local.AuthManager;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.request.LoginRequest;
import com.uap.data.remote.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    private ProgressBar progress;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AuthManager.isLoggedIn(this)) {
            if(AuthManager.getRole(this).equalsIgnoreCase("lecturer") || AuthManager.getRole(this).equalsIgnoreCase("lecture")){
                startActivity(new Intent(this, TeacherMainActivity.class));
            }else{
                startActivity(new Intent(this, StudentMainActivity.class));
            }
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        rootView = findViewById(android.R.id.content);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        progress = findViewById(R.id.progress);

        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String u = edtUser.getText().toString().trim();
        String p = edtPass.getText().toString().trim();

        if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
            showError("Nhập đầy đủ tài khoản & mật khẩu");
            return;
        }

        setLoading(true);

        ApiService.getSOService().login(new LoginRequest(u, p)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> res) {
                setLoading(false);
                if (!res.isSuccessful() || res.body() == null) {
                    showError("Đăng nhập thất bại, kiểm tra lại tài khoản!");
                    return;
                }

                LoginResponse body = res.body();
                if (body.getToken() == null || body.getToken().isEmpty()) {
                    showError("Thiếu token trong phản hồi từ server");
                    return;
                }

                Log.d("LOGIN", "Login ok: " + body.getMessage());

                String role = body.getUser() != null ? body.getUser().getRole() : null;
                boolean needChange = body.isPasswordChangeRequired();

                AuthManager.saveLogin(
                        LoginActivity.this,
                        body.getToken(),
                        role,
                        needChange
                );

                showSuccess("Đăng nhập thành công");

                if (needChange) {
                    startActivity(new Intent(LoginActivity.this, PasswordChangeActivity.class));
                    finish();
                    return;
                }

                if (role != null && (role.equalsIgnoreCase("lecturer") || role.equalsIgnoreCase("lecture"))) {
                    Intent i = new Intent(LoginActivity.this, TeacherMainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(LoginActivity.this, StudentMainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                Log.d("Login","Lỗi mạng: " + t.getMessage());
                showError("Không kết nối được server: " + t.getMessage());
            }
        });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            edtUser.setEnabled(false);
            edtPass.setEnabled(false);
        } else {
            progress.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            edtUser.setEnabled(true);
            edtPass.setEnabled(true);
        }
    }

    private void showError(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
        // Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void showSuccess(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
    }
}
