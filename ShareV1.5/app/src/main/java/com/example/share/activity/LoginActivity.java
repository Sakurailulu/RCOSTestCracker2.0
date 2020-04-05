package com.example.share.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.share.R;
import com.example.share.util.Config;
import com.example.share.util.OkHttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btRegister);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> map = new HashMap<>();
                            map.put("username", username);
                            map.put("password", password);
                            String response = OkHttpUtil.post(Config.URL + "/user/login", map);
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("data");

                            if (result.equals("1")) {
                                SharedPreferences.Editor editor = getSharedPreferences().edit();
                                editor.putString("username", username);
                                editor.putString("password", password);
                                editor.apply();
                                toast("Login success");

                                finish();
                            } else {
                                toast("User name or password error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
