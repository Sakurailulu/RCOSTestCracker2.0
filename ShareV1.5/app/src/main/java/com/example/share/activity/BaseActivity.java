package com.example.share.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config", 0);
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    protected void toast(String text) {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = text;
        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    Toast.makeText(BaseActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
