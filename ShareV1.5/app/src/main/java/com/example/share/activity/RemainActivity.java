package com.example.share.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.share.R;
import com.example.share.adapter.RemainAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemainActivity extends BaseActivity {

    private EditText etCourse;
    private TextView tvDate;
    private Button btSubmit;

    private ListView lvRemain;
    private RemainAdapter remainAdapter;
    private List<String> remainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remain);

        init();
    }

    private void init() {
        etCourse = findViewById(R.id.etCourse);
        tvDate = findViewById(R.id.tvDate);
        btSubmit = findViewById(R.id.btSubmit);

        lvRemain = findViewById(R.id.lvRemain);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(RemainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        String monthStr;
                        String dayStr;
                        monthStr = month < 10 ? "0" + month : String.valueOf(month);
                        dayStr = day < 10 ? "0" + day : String.valueOf(day);
                        String dateStr = year + "-" + monthStr + "-" + dayStr;

                        tvDate.setText(dateStr);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = tvDate.getText().toString();
                String course = etCourse.getText().toString();

                if (date.equals("") || course.equals("")) {
                    toast("Date and course can not be null");
                    return;
                }
                remainList.add(date + "&&" + course);
                Set<String> stringSet = new HashSet<>();
                stringSet.addAll(remainList);

                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putStringSet("remainSet", stringSet);
                editor.apply();
                loadData();

                tvDate.setText("");
                etCourse.setText("");
                toast("Submit success");
            }
        });

        lvRemain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;

                new AlertDialog.Builder(RemainActivity.this)
                        .setTitle("WARNING")
                        .setMessage("Do you want to remove this course remain?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Set<String> stringSet = getSharedPreferences().getStringSet("remainSet", new HashSet<String>());
                                stringSet.remove(remainList.get(position));
                                SharedPreferences.Editor editor = getSharedPreferences().edit();
                                editor.putStringSet("remainSet", stringSet);
                                editor.apply();
                                loadData();
                            }
                        })
                        .setNegativeButton("NO", null).show();
            }
        });

        loadData();
    }

    private void loadData() {
        remainList = new ArrayList<>();
        Set<String> stringSet = getSharedPreferences().getStringSet("remainSet", new HashSet<String>());
        remainList.addAll(stringSet);
        remainAdapter = new RemainAdapter(this, remainList);
        lvRemain.setAdapter(remainAdapter);
    }
}
