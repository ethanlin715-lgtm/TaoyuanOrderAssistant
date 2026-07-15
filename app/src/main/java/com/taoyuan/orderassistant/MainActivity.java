package com.taoyuan.orderassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Switch serviceToggle;
    private ListView keywordsList;
    private Button addKeywordBtn;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> adapter;
    private List<String> keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceToggle = findViewById(R.id.service_toggle);
        keywordsList = findViewById(R.id.keywords_list);
        addKeywordBtn = findViewById(R.id.add_keyword_btn);

        sharedPreferences = getSharedPreferences("LineAssistant", MODE_PRIVATE);
        keywords = new ArrayList<>(sharedPreferences.getStringSet("keywords", new HashSet<>()));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keywords);
        keywordsList.setAdapter(adapter);

        // 服務切換
        serviceToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startAccessibilityService();
            } else {
                stopAccessibilityService();
            }
        });

        // 新增關鍵字
        addKeywordBtn.setOnClickListener(v -> {
            // 簡單示例：添加預設關鍵字
            String newKeyword = "新關鍵字";
            if (!keywords.contains(newKeyword)) {
                keywords.add(newKeyword);
                adapter.notifyDataSetChanged();
                saveKeywords();
                Toast.makeText(this, "已添加關鍵字", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "請在設定中啟用「桃園搶單助手」服務", Toast.LENGTH_LONG).show();
    }

    private void stopAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "請在設定中停用「桃園搶單助手」服務", Toast.LENGTH_LONG).show();
    }

    private void saveKeywords() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("keywords", new HashSet<>(keywords));
        editor.apply();
    }
}
