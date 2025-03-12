package vn.edu.fpt.instalite.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.database.DatabaseHelper;
import vn.edu.fpt.instalite.models.User;
import vn.edu.fpt.instalite.services.AuthService;
import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView textSignUp;
    private Button btnLogin;
    private AuthService authService;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authService = new AuthService(this);

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                try {
                    User result = authService.login(username, password);
                    if (result != null) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("log user login", String.valueOf(result.toString()));
                        DataLocalManager.setUser(result);
                        // Chuyển sang màn hình chính hoặc màn hình tiếp theo
                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                         startActivity(intent);
                         finish(); // Đóng LoginActivity nếu bạn không muốn người dùng quay lại nó
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu chưa chính xác!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IllegalArgumentException e) {
                    // Bắt ngoại lệ khi thông tin nhập vào không đầy đủ
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Bắt các ngoại lệ khác nếu có
                    Toast.makeText(LoginActivity.this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang SignUpActivity bằng Intent
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                // thêm hiệu ứng chuyển tiếp
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Thiết lập sự kiện lắng nghe cho việc nhấn vào drawableRight
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(etPassword);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textSignUp = findViewById(R.id.textSignUp);
    }

    private void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        editText.setSelection(editText.getText().length());
    }

}