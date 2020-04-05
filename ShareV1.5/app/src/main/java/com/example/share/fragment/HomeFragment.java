package com.example.share.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.share.R;
import com.example.share.activity.CourseActivity;
import com.example.share.activity.CourseAddActivity;
import com.example.share.activity.LoginActivity;
import com.example.share.adapter.CourseAdapter;
import com.example.share.model.Course;
import com.example.share.util.Config;
import com.example.share.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HomeFragment extends BaseFragment {

    private Activity activity;

    private String username;
    private String password;

    private Button btAdd;
    private ListView lvCourse;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        username = getSharedPreferences().getString("username", "");
        password = getSharedPreferences().getString("password", "");

        loadData();
    }

    private void init(View view) {
        courseList = new ArrayList<>();
        btAdd = view.findViewById(R.id.btAdd);
        lvCourse = view.findViewById(R.id.lvCourse);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.equals("") || password.equals("")) {
                    toast("Please login first.");
                    Intent i = new Intent(activity, LoginActivity.class);
                    startActivity(i);
                    return;
                }

                Intent i = new Intent(activity, CourseAddActivity.class);
                startActivity(i);
            }
        });

        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (username.equals("") || password.equals("")) {
                    toast("Please login first.");
                    Intent i = new Intent(activity, LoginActivity.class);
                    startActivity(i);
                    return;
                }

                Intent i = new Intent(activity, CourseActivity.class);
                i.putExtra("course", courseList.get(position));
                startActivity(i);
            }
        });
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<>();
                    String response = OkHttpUtil.post(Config.URL + "/course/selectList", map);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseJsonArray = jsonObject.getJSONArray("data");

                    courseList = new ArrayList<>();
                    for (int i = 0; i < courseJsonArray.length(); i++) {
                        JSONObject courseJsonObject = courseJsonArray.getJSONObject(i);
                        Course course = new Course();
                        course.setId(Integer.parseInt(courseJsonObject.getString("id")));
                        course.setName(courseJsonObject.getString("name"));
                        courseList.add(course);
                    }

                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    courseAdapter = new CourseAdapter(activity, courseList);
                    lvCourse.setAdapter(courseAdapter);
                }
            }
        }
    };
}
