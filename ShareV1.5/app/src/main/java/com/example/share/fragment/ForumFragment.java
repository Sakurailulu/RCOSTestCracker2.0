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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.share.R;
import com.example.share.activity.CourseActivity;
import com.example.share.activity.LoginActivity;
import com.example.share.activity.TopicActivity;
import com.example.share.activity.TopicAddActivity;
import com.example.share.adapter.TopicAdapter;
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

public class ForumFragment extends BaseFragment {

    private Activity activity;

    private String username;
    private String password;

    private Button btAdd;
    private ListView lvTopic;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
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
        topicList = new ArrayList<>();
        btAdd = view.findViewById(R.id.btAdd);
        lvTopic = view.findViewById(R.id.lvTopic);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.equals("") || password.equals("")) {
                    toast("Please login first.");
                    Intent i = new Intent(activity, LoginActivity.class);
                    startActivity(i);
                    return;
                }

                Intent i = new Intent(activity, TopicAddActivity.class);
                startActivity(i);
            }
        });

        lvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (username.equals("") || password.equals("")) {
                    toast("Please login first.");
                    Intent i = new Intent(activity, LoginActivity.class);
                    startActivity(i);
                    return;
                }

                Intent i = new Intent(activity, TopicActivity.class);
                i.putExtra("topic", topicList.get(position));
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
                    String response = OkHttpUtil.post(Config.URL + "/topic/selectList", map);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray topicJsonArray = jsonObject.getJSONArray("data");

                    topicList = new ArrayList<>();
                    for (int i = 0; i < topicJsonArray.length(); i++) {
                        JSONObject topicJsonObject = topicJsonArray.getJSONObject(i);
                        JSONObject userJsonObject = topicJsonObject.getJSONObject("user");
                        User user = new User();
                        user.setId(Integer.parseInt(userJsonObject.getString("id")));
                        user.setName(userJsonObject.getString("name"));
                        Topic topic = new Topic();
                        topic.setUser(user);
                        topic.setId(Integer.parseInt(topicJsonObject.getString("id")));
                        topic.setTitle(topicJsonObject.getString("title"));
                        topic.setContent(topicJsonObject.getString("content"));
                        topic.setCreateTime(topicJsonObject.getString("createTime"));
                        topicList.add(topic);
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
                    topicAdapter = new TopicAdapter(activity, topicList);
                    lvTopic.setAdapter(topicAdapter);
                }
            }
        }
    };
}
