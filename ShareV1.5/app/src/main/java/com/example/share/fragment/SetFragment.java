package com.example.share.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.share.R;
import com.example.share.activity.LoginActivity;
import com.example.share.activity.RemainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SetFragment extends BaseFragment {

    private Activity activity;

    private String username;
    private String password;

    private TextView tvLogin;
    private TextView tvRemind;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        username = getSharedPreferences().getString("username", "");
        password = getSharedPreferences().getString("password", "");
    }

    private void init(View view) {

        tvLogin = view.findViewById(R.id.tvLogin);
        tvRemind = view.findViewById(R.id.tvRemind);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(activity)
                        .setTitle("WARNING")
                        .setMessage("Do you want to logout and return to login?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences().edit();
                                editor.clear();
                                editor.apply();
                                Intent i = new Intent(activity, LoginActivity.class);
                                startActivity(i);
                                toast("Logout successfully");
                            }
                        })
                        .setNegativeButton("NO", null).show();


            }
        });

        tvRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, RemainActivity.class);
                startActivity(i);
            }
        });
    }
}
