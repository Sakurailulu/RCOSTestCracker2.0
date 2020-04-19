package com.example.share.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.share.R;
import com.example.share.adapter.CommentAdapter;
import com.example.share.model.Comment;
import com.example.share.model.Topic;
import com.example.share.model.User;
import com.example.share.util.Config;
import com.example.share.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicActivity extends BaseActivity {

    private Topic topic;
    private TextView tvTitle;
    private TextView tvContent;
    private ListView lvFolder;
    private EditText etComment;
    private Button btSubmit;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private String username;
    private String password;

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        init();
    }

    private void init() {
        username = getSharedPreferences().getString("username", "");
        password = getSharedPreferences().getString("password", "");

        topic = (Topic) getIntent().getSerializableExtra("topic");
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        lvFolder = findViewById(R.id.lvFolder);
        etComment = findViewById(R.id.etComment);
        btSubmit = findViewById(R.id.btSubmit);

        tvTitle.setText(topic.getTitle());
        tvContent.setText(topic.getContent());

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = etComment.getText().toString();
                if (content.equals("")) {
                    toast("Please enter your opinion first");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> map = new HashMap<>();
                            map.put("content", content);
                            map.put("state", "1");
                            map.put("topicId", topic.getId().toString());
                            map.put("username", username);
                            map.put("password", password);
                            String response = OkHttpUtil.post(Config.URL + "/comment/add", map);
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("data");

                            if (result.equals("1")) {
                                loadData();
                                toast("Submit success");
                            } else {
                                toast("Submit fail");
                            }
                            handler.sendEmptyMessage(1);
                        } catch (Exception e) {
                            toast("Submit fail");
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("topicId", topic.getId().toString());
                    String response = OkHttpUtil.post(Config.URL + "/comment/selectListByTopicId", map);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray commentJsonArray = jsonObject.getJSONArray("data");

                    commentList = new ArrayList<>();
                    for (int i = 0; i < commentJsonArray.length(); i++) {
                        JSONObject commentJsonObject = commentJsonArray.getJSONObject(i);
                        JSONObject userJsonObject = commentJsonObject.getJSONObject("user");
                        User user = new User();
                        user.setId(Integer.parseInt(userJsonObject.getString("id")));
                        user.setName(userJsonObject.getString("name"));
                        Comment comment = new Comment();
                        comment.setUser(user);
                        comment.setId(commentJsonObject.getInt("id"));
                        comment.setContent(commentJsonObject.getString("content"));
                        comment.setCreateTime(commentJsonObject.getString("createTime"));
                        commentList.add(comment);
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
                    commentAdapter = new CommentAdapter(TopicActivity.this, commentList);
                    lvFolder.setAdapter(commentAdapter);
                }
                case 1: {
                    etComment.setText("");
                    break;
                }
            }
        }
    };
}
