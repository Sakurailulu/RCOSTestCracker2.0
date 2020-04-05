package com.example.share.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.share.R;
import com.example.share.adapter.FolderAdapter;
import com.example.share.model.Course;
import com.example.share.model.Folder;
import com.example.share.util.Config;
import com.example.share.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class CourseActivity extends BaseActivity {

    private Course course;
    private TextView tvCourse;
    private ListView lvFolder;
    private ImageView ivAdd;

    private FolderAdapter folderAdapter;
    private List<Folder> folderList;

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        init();
    }

    private void init() {
        course = (Course) getIntent().getSerializableExtra("course");
        tvCourse = findViewById(R.id.tvCourse);
        lvFolder = findViewById(R.id.lvFolder);
        ivAdd = findViewById(R.id.ivAdd);

        tvCourse.setText(course.getName());

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CourseActivity.this, FolderAddActivity.class);
                i.putExtra("id", course.getId());
                startActivity(i);
            }
        });

        lvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(CourseActivity.this, FolderActivity.class);
                i.putExtra("folder", folderList.get(position));
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
                    map.put("courseId", course.getId().toString());
                    String response = OkHttpUtil.post(Config.URL + "/folder/selectListByCourseId", map);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray courseJsonArray = jsonObject.getJSONArray("data");

                    folderList = new ArrayList<>();
                    for (int i = 0; i < courseJsonArray.length(); i++) {
                        JSONObject courseJsonObject = courseJsonArray.getJSONObject(i);
                        Folder folder = new Folder();
                        folder.setId(courseJsonObject.getInt("id"));
                        folder.setTitle(courseJsonObject.getString("title"));
                        folder.setContent(courseJsonObject.getString("content"));
                        folder.setPath(courseJsonObject.getString("path"));
                        folder.setFileName(courseJsonObject.getString("fileName"));
                        folder.setLikeNum(courseJsonObject.getInt("likeNum"));
                        folder.setDislikeNum(courseJsonObject.getInt("dislikeNum"));
                        folderList.add(folder);
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
                    folderAdapter = new FolderAdapter(CourseActivity.this, folderList);
                    lvFolder.setAdapter(folderAdapter);
                }
            }
        }
    };
}
