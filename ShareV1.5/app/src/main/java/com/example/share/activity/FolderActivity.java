package com.example.share.activity;

import android.annotation.SuppressLint;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.share.R;
import com.example.share.adapter.OpinionAdapter;
import com.example.share.model.Folder;
import com.example.share.model.Opinion;
import com.example.share.util.Config;
import com.example.share.util.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FolderActivity extends BaseActivity {

    private Folder folder;
    private TextView tvFolder;
    private ListView lvOpinion;
    private Button btLike;
    private Button btDislike;
    private Button btDownload;
    private Button btSubmit;
    private EditText etContent;

    private OpinionAdapter opinionAdapter;
    private List<Opinion> opinionList;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        init();
    }

    private void init() {
        username = getSharedPreferences().getString("username", "");
        password = getSharedPreferences().getString("password", "");

        folder = (Folder) getIntent().getSerializableExtra("folder");
        tvFolder = findViewById(R.id.tvFolder);
        lvOpinion = findViewById(R.id.lvOpinion);
        btLike = findViewById(R.id.btLike);
        btDislike = findViewById(R.id.btDislike);
        btDownload = findViewById(R.id.btDownload);
        btSubmit = findViewById(R.id.btSubmit);
        etContent = findViewById(R.id.etContent);

        tvFolder.setText(folder.getTitle());
        btLike.setText("LIKE: " + folder.getLikeNum());
        btDislike.setText("DISLIKE: " + folder.getDislikeNum());

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });

        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAttitude("1");
            }
        });

        btDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAttitude("-1");
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String content = etContent.getText().toString();
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
                            map.put("folderId", folder.getId().toString());
                            map.put("username", username);
                            map.put("password", password);
                            String response = OkHttpUtil.post(Config.URL + "/opinion/sendOpinion", map);
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("data");

                            if (result.equals("1")) {
                                loadData();
                                toast("Submit success");
                            } else {
                                toast("Submit fail");
                            }
                            handler.sendEmptyMessage(2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        loadData();
    }

    private void sendAttitude(final String attitude) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("attitude", attitude);
                    map.put("folderId", folder.getId().toString());
                    map.put("username", username);
                    map.put("password", password);
                    String response = OkHttpUtil.post(Config.URL + "/opinion/sendAttitude", map);
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("data");

                    if (result.equals("1")) {
                        toast("Submit success");
                        if (attitude.equals("1")) {
                            folder.setLikeNum(folder.getLikeNum() + 1);
                        } else if (attitude.equals("-1")) {
                            folder.setDislikeNum(folder.getDislikeNum() + 1);
                        }
                        handler.sendEmptyMessage(1);
                    } else {
                        toast("You have shown your attitude");
                    }

                    etContent.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("folderId", folder.getId().toString());
                    String response = OkHttpUtil.post(Config.URL + "/opinion/selectListByFolderId", map);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray opinionJsonArray = jsonObject.getJSONArray("data");

                    opinionList = new ArrayList<>();
                    for (int i = 0; i < opinionJsonArray.length(); i++) {
                        JSONObject courseJsonObject = opinionJsonArray.getJSONObject(i);
                        Opinion opinion = new Opinion();
                        opinion.setContent(courseJsonObject.getString("content"));
                        opinionList.add(opinion);
                    }

                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void download() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.HOURS)
                .readTimeout(1, TimeUnit.HOURS)
                .build();

        Request request = new Request.Builder()
                .url(Config.URL + "/folder/download")
                .addHeader("path", folder.getPath())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/ShareFile");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, folder.getFileName());
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    MediaScannerConnection.scanFile(FolderActivity.this, new String[]{file.getAbsolutePath()}, null, null);
                    toast("Download success. PATH:" + Environment.getExternalStorageDirectory().getPath() + "/ShareFile/" + folder.getFileName());
                } catch (Exception e) {
                    e.printStackTrace();
                    toast("Download fail");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    opinionAdapter = new OpinionAdapter(FolderActivity.this, opinionList);
                    lvOpinion.setAdapter(opinionAdapter);
                    break;
                }
                case 1: {
                    btLike.setText("LIKE: " + folder.getLikeNum());
                    btDislike.setText("DISLIKE: " + folder.getDislikeNum());
                    break;
                }
                case 2: {
                    etContent.setText("");
                    break;
                }
            }
        }
    };
}
