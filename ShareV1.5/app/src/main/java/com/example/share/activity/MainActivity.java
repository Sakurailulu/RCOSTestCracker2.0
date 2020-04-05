package com.example.share.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.share.R;
import com.example.share.fragment.HomeFragment;
import com.example.share.fragment.SetFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends BaseActivity {

    private Fragment homeFragment;
    private Fragment setFragment;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        homeFragment = new HomeFragment();
        setFragment = new SetFragment();

        RadioGroup rgMain = findViewById(R.id.rgMain);
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHome: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.flMain, homeFragment).commit();
                        break;
                    }
                    case R.id.rbSet: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.flMain, setFragment).commit();
                        break;
                    }
                }
            }
        });
        rgMain.check(R.id.rbHome);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "app1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "app1", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        String nowStr = simpleDateFormat.format(new Date());
        Set<String> remainSet = getSharedPreferences().getStringSet("remainSet", new HashSet<String>());
        int i = 0;
        for (String remain : remainSet) {
            try {
                i++;
                String[] remains = remain.split("&&");
                String dateStr = remains[0];
                String course = remains[1];

                if (nowStr.equals(dateStr)) {
                    Notification notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                            .setContentTitle("Course remain")
                            .setContentText(course + "  " + dateStr)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .build();
                    manager.notify(i, notification);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        requestPermission();
    }


    //动态获取定位所需权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 ) {

            List<String> permissionsList = new ArrayList<>();

            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET
            };

            for (String perm : permissions) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
                    // 进入到这里代表没有权限.
                    permissionsList.add(perm);
                }
            }

            if (!permissionsList.isEmpty()) {
                requestPermissions(permissionsList.toArray(new String[0]), 0);
            }
        }
    }
}
