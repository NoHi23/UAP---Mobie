    package com.uap.ui;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.uap.R;
    import com.uap.auth.AuthManager;
    import com.uap.data.remote.ApiService;
    import com.uap.data.remote.request.LoginRequest;
    import com.uap.data.remote.response.LoginResponse;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class LoginActivity extends AppCompatActivity {
        private EditText edtUser, edtPass;
        private Button btnLogin;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Nếu đã login rồi -> vào thẳng Main
            if (AuthManager.isLoggedIn(this)) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }

            setContentView(R.layout.activity_login);
            edtUser = findViewById(R.id.edtUser);
            edtPass = findViewById(R.id.edtPass);
            btnLogin = findViewById(R.id.btnLogin);

            btnLogin.setOnClickListener(v -> doLogin());
        }

        private void doLogin() {
            String u = edtUser.getText().toString().trim();
            String p = edtPass.getText().toString().trim();

            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Toast.makeText(this, "Nhập đầy đủ tài khoản & mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            //setLoading(true);

            ApiService.getSOService().login(new LoginRequest(u, p)).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> res) {
                    //setLoading(false);
                    if (!res.isSuccessful() || res.body() == null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                        return;
                    }

                    LoginResponse body = res.body();
                    if (body.getToken() == null || body.getToken().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Thiếu token trong phản hồi", Toast.LENGTH_LONG).show();
                        return;
                    }

                    AuthManager.saveToken(LoginActivity.this, body.getToken());

                    if (body.isPasswordChangeRequired()) {
                        Intent i = new Intent(LoginActivity.this, PasswordChangeActivity.class);
                        i.putExtra("token", body.getToken());
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                   // setLoading(false);
                    Log.d("Login","Lỗi mạng: " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }