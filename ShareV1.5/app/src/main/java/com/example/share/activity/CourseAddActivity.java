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

public class CourseAddActivity extends BaseActivity {

    private EditText etName;
    private Button btSubmit;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);

        init();
    }

    private void init() {
        username = getSharedPreferences().getString("username", "");
        password = getSharedPreferences().getString("password", "");

        etName = findViewById(R.id.etName);
        btSubmit = findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = etName.getText().toString();
                if (name.equals("")) {
                    toast("Name can not be null");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("name", name);
                                        map.put("username", username);
                                        map.put("password", password);
                                        String response = OkHttpUtil.post(Config.URL + "/course/add", map);
                                        JSONObject jsonObject = new JSONObject(response);
                                        String result = jsonObject.getString("data");

                                        if (result.equals("1")) {
                                            toast("Submit success");
                                            finish();
                                        } else {
                                            toast("Submit error");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        toast("Error");
                                    }
                                }
                            }).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
