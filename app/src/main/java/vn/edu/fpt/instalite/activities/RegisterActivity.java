package vn.edu.fpt.instalite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.fpt.instalite.R;
import vn.edu.fpt.instalite.dto.RegisterDTO;
import vn.edu.fpt.instalite.services.AuthService;
import vn.edu.fpt.instalite.utils.Common;

public class RegisterActivity extends AppCompatActivity {
    private TextView textLoginPage;
    private EditText etUsername, etPassword, etEmail, etRePassword;
    private Button btnRegister;
    private AuthService authService;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        authService = new AuthService(this);
        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String repass = etRePassword.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                try {
                    boolean result = authService.register(new RegisterDTO(username, email, password, repass));
                    if (result) {
                        Common.showToast(RegisterActivity.this, "Register successfully! You can login right now.");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (IllegalArgumentException e) {
                    Common.showToast(RegisterActivity.this, e.getMessage());
                } catch (Exception e) {
                    Common.showToast(RegisterActivity.this, "An error occurred: " + e.getMessage());
                }
            }
        });


        textLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRePassword = findViewById(R.id.etRePassword);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);
        textLoginPage = findViewById(R.id.textLoginPage);
    }

}
