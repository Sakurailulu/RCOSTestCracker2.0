package com.example.share.activity;

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

public class RegisterActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etName;
    private EditText etPhone;
    private Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btSubmit = findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String phone = etPhone.getText().toString();

                if (username.equals("") || password.equals("") || name.equals("") || phone.equals("")) {
                    toast(" Form cannot contain empty items");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> map = new HashMap<>();
                            map.put("username", username);
                            map.put("password", password);
                            map.put("name", name);
                            map.put("phone", phone);
                            String response = OkHttpUtil.post(Config.URL + "/user/register", map);
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("data");

                            if (result.equals("1")) {
                                toast("Register success");
                                finish();
                            } else if (result.equals("2")) {
                                toast("Username is already registered");
                            } else {
                                toast("Error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
